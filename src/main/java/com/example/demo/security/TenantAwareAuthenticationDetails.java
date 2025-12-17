package com.example.demo.security;


import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TenantAwareAuthenticationDetails extends WebAuthenticationDetails {
    private final String tenantId;
    private final String userAgent;
    private final String deviceType;
    private final LocalDateTime loginAttemptTime;

    public TenantAwareAuthenticationDetails(HttpServletRequest request, String tenantId) {
        super(request);

        this.tenantId = tenantId;
        this.userAgent = request.getHeader("User-Agent");
        this.deviceType = determineDeviceType(this.userAgent);
        this.loginAttemptTime = LocalDateTime.now();
    }

    private String determineDeviceType(String userAgent) {
        if (userAgent == null) return "UNKNOWN";
        String ua = userAgent.toLowerCase();
        if (ua.contains("mobile") || ua.contains("android") || ua.contains("iphone")) return "MOBILE";
        if (ua.contains("tablet") || ua.contains("ipad")) return "TABLET";
        return "DESKTOP";
    }

}