CREATE TABLE user_sessions (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    refresh_token TEXT NOT NULL UNIQUE,

    device_name VARCHAR(255),

    ip_address VARCHAR(100),

    user_agent TEXT,

    expires_at TIMESTAMP NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    last_used_at TIMESTAMP,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_sessions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);


CREATE INDEX idx_user_sessions_user_id 
ON user_sessions(user_id);


CREATE INDEX idx_user_sessions_refresh_token
ON user_sessions(refresh_token);