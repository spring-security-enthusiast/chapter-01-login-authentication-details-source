package com.example.demo.controller;

import com.example.demo.model.TenantUser;
import com.example.demo.service.TenantUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final TenantUserService service;

    @Value("${app.tenancy.base-domain:localhost}")
    private String baseDomain;


    @GetMapping(value = {"/", "/index", "/index.html"})
    public String index(HttpServletRequest request, Model model) {
        String scheme = request.getScheme();      // http / https
        int port = request.getServerPort();       // 8080
        String portPart = isDefaultPort(scheme, port) ? "" : ":" + port;

        List<TenantUser> tenants = service.getTenantUsers();
        tenants.forEach(u -> {
            u.setLoginUrl(scheme + "://" + u.getTenantId() + "." + baseDomain + portPart + "/auth/login");
        });

        model.addAttribute("tenants", tenants);
        return "index";
    }

    /**
     * Router endpoint:
     * /go?tenant=acme  ->  redirects to  http(s)://acme.<baseDomain>:<port>/auth/login
     */
    @GetMapping("/go")
    public String go(@RequestParam("tenant") String tenant, HttpServletRequest request) {
        String scheme = request.getScheme();     // http / https
        int port = request.getServerPort();      // 8080
        String portPart = isDefaultPort(scheme, port) ? "" : ":" + port;

        // Basic safety: keep tenant slug clean (avoid open redirect / weird hostnames)
        String t = tenant == null ? "" : tenant.trim().toLowerCase();
        if (!t.matches("[a-z0-9-]{2,30}")) {
            return "redirect:/?error=invalid_tenant";
        }

        return "redirect:" + scheme + "://" + t + "." + baseDomain + portPart + "/auth/login";
    }

    private boolean isDefaultPort(String scheme, int port) {
        return ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
    }
}
