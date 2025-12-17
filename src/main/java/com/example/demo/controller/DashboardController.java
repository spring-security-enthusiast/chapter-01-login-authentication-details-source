package com.example.demo.controller;

import com.example.demo.security.TenantAwareAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, HttpServletRequest request, Model model) {
        String host = request.getServerName();       //  acme.localhost
        String tenant = host.split("\\.")[0];  // "acme" or "globex"

        boolean isAcme = "acme".equalsIgnoreCase(tenant);

        model.addAttribute("tenant", tenant.toUpperCase());
        model.addAttribute("brandName", isAcme ? "ACME Bank" : "Globex Finance");
        model.addAttribute("theme", isAcme ? "acme" : "globex"); // used as CSS hook
        model.addAttribute("tagline", isAcme ? "Operations Dashboard" : "Insights Dashboard");
        model.addAttribute("username", authentication.getName());

        Object detailsObj = authentication.getDetails();
        if (detailsObj instanceof TenantAwareAuthenticationDetails details) {
            model.addAttribute("tenantId", details.getTenantId());
            model.addAttribute("deviceType", details.getDeviceType());
            model.addAttribute("loginAttemptTime", details.getLoginAttemptTime());
            model.addAttribute("ipAddress", details.getRemoteAddress());
            model.addAttribute("sessionId", details.getSessionId());
            model.addAttribute("userAgent", details.getUserAgent());
        }

        return "dashboard";
    }

}
