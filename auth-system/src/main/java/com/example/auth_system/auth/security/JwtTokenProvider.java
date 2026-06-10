package com.example.auth_system.auth.security;

import com.example.auth_system.auth.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret:404D635166546A576E5A7234753778214125442A472D4B6150645367566B5970}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration; // 24 hours default

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpiration; // 7 days default

    // Token blacklist for invalidated tokens (use Redis in production)
    private final Map<String, Long> invalidatedTokens = new ConcurrentHashMap<>();

    /**
     * Generate JWT token for User entity
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("type", "access");

        return buildToken(claims, user.getEmail(), jwtExpiration);
    }

    /**
     * Generate JWT token from UserDetails (Spring Security)
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        
        // Extract authorities/roles
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        claims.put("roles", authorities);
        claims.put("type", "access");

        return buildToken(claims, userDetails.getUsername(), jwtExpiration);
    }

    /**
     * Generate refresh token
     */
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("type", "refresh");
        claims.put("isRefreshToken", true);

        return buildToken(claims, user.getEmail(), refreshExpiration);
    }

    /**
     * Generate refresh token from UserDetails
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        claims.put("isRefreshToken", true);

        return buildToken(claims, userDetails.getUsername(), refreshExpiration);
    }

    /**
     * Generate password reset token (1 hour expiration)
     */
    public String generatePasswordResetToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "password_reset");
        claims.put("userId", user.getId().toString());
        claims.put("purpose", "reset_password");

        return buildToken(claims, user.getEmail(), 3600000); // 1 hour
    }

    /**
     * Generate email verification token
     */
    public String generateEmailVerificationToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "email_verification");
        claims.put("userId", user.getId().toString());
        claims.put("purpose", "verify_email");

        return buildToken(claims, user.getEmail(), 86400000); // 24 hours
    }

    /**
     * Build token with claims (refactored method)
     */
    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Get signing key (cached for performance)
     */
    private Key getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Get username/email from token
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get user email from token (alias for getUsernameFromToken)
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Get user ID from token
     */
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }

    /**
     * Get role from token
     */
    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    /**
     * Get token type (access, refresh, password_reset, email_verification)
     */
    public String getTokenType(String token) {
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }

    /**
     * Check if token is refresh token
     */
    public boolean isRefreshToken(String token) {
        String tokenType = getTokenType(token);
        return "refresh".equals(tokenType);
    }

    /**
     * Check if token is access token
     */
    public boolean isAccessToken(String token) {
        String tokenType = getTokenType(token);
        return "access".equals(tokenType);
    }

    /**
     * Get expiration date from token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Get specific claim from token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Get all claims from token with proper error handling
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validate JWT token (basic validation)
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            
            // Check if token is blacklisted
            if (isTokenInvalidated(token)) {
                log.warn("Token has been invalidated");
                return false;
            }
            
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Validate JWT token with UserDetails
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
        if (isValid && isTokenInvalidated(token)) {
            log.warn("Token is invalidated for user: {}", username);
            return false;
        }
        
        return isValid;
    }

    /**
     * Validate token for specific purpose (e.g., password reset)
     */
    public boolean validateTokenForPurpose(String token, String expectedPurpose) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String purpose = claims.get("purpose", String.class);
            String type = claims.get("type", String.class);
            
            return expectedPurpose.equals(purpose) && !isTokenExpired(token) && !isTokenInvalidated(token);
        } catch (Exception e) {
            log.error("Token validation for purpose failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Get remaining time in milliseconds
     */
    public long getRemainingTimeInMillis(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            long remaining = expiration.getTime() - System.currentTimeMillis();
            return Math.max(remaining, 0);
        } catch (Exception e) {
            log.error("Error getting remaining time: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Get expiration time in milliseconds
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Get refresh expiration time in milliseconds
     */
    public long getRefreshExpirationTime() {
        return refreshExpiration;
    }

    /**
     * Invalidate token (add to blacklist)
     * In production, use Redis with TTL
     */
    public void invalidateToken(String token) {
        try {
            Date expiration = getExpirationDateFromToken(token);
            long ttl = expiration.getTime() - System.currentTimeMillis();
            
            if (ttl > 0) {
                invalidatedTokens.put(token, System.currentTimeMillis() + ttl);
                log.info("Token invalidated successfully. Will be removed after {} ms", ttl);
            } else {
                log.warn("Attempted to invalidate already expired token");
            }
        } catch (Exception e) {
            log.error("Error invalidating token: {}", e.getMessage());
        }
    }

    /**
     * Check if token is invalidated
     */
    public boolean isTokenInvalidated(String token) {
        Long invalidationTime = invalidatedTokens.get(token);
        if (invalidationTime == null) {
            return false;
        }
        
        // Remove expired entries from blacklist
        if (System.currentTimeMillis() > invalidationTime) {
            invalidatedTokens.remove(token);
            return false;
        }
        
        return true;
    }

    /**
     * Clean up expired tokens from blacklist (can be called by scheduled task)
     */
    public void cleanupBlacklist() {
        long now = System.currentTimeMillis();
        int removedCount = 0;
        
        for (Map.Entry<String, Long> entry : invalidatedTokens.entrySet()) {
            if (now > entry.getValue()) {
                invalidatedTokens.remove(entry.getKey());
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            log.debug("Cleaned up {} expired tokens from blacklist", removedCount);
        }
    }

    /**
     * Refresh token - generate new access token from refresh token
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        
        if (!isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Token is not a refresh token");
        }
        
        String email = getEmailFromToken(refreshToken);
        String userId = getUserIdFromToken(refreshToken);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("type", "access");
        
        return buildToken(claims, email, jwtExpiration);
    }

    /**
     * Extract JWT token from Authorization header
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}