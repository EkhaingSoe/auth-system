package com.example.auth_system.common.util;

import org.springframework.stereotype.Component;

@Component
public class DeviceUtils {
    public static String getDeviceName(String userAgent) {

        String browser = "Unknown Browser";
        String os = "Unknown OS";

        // Browser
        if (userAgent.contains("Edg")) {
            browser = "Edge";
        } else if (userAgent.contains("Chrome")) {
            browser = "Chrome";
        } else if (userAgent.contains("Firefox")) {
            browser = "Firefox";
        } else if (userAgent.contains("Safari")) {
            browser = "Safari";
        }

        // Operating System
        if (userAgent.contains("Windows")) {
            os = "Windows";
        } else if (userAgent.contains("Android")) {
            os = "Android";
        } else if (userAgent.contains("iPhone")) {
            os = "iPhone";
        } else if (userAgent.contains("Mac OS")) {
            os = "macOS";
        } else if (userAgent.contains("Linux")) {
            os = "Linux";
        }

        return browser + " on " + os;
    }
}
