package com.example.demo.controller;

import com.example.demo.resolver.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final TenantResolver tenantResolver;

    @Value("${app.tenancy.base-domain:localhost}")
    private String publicRootHost;


    @GetMapping("/auth/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                HttpServletRequest request,
                                Model model,
                                Authentication authentication) {

        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        String tenantId = tenantResolver.fromHost(request.getServerName());
        model.addAttribute("tenantId", tenantId);

        String indexUrl = request.getScheme() + "://" + publicRootHost + ":" + request.getServerPort() + "/";
        model.addAttribute("indexUrl", indexUrl); // for your "Back to index" link

        return "auth/login";
    }


    @PostMapping("/customSuccessPage")
    public String customSuccessPage(Model model, Principal principal, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        model.addAttribute("username", principal.getName());
        model.addAttribute("ipAddress", ipAddress);
        model.addAttribute("userAgent", userAgent);
        return "dashboard";
    }
}
