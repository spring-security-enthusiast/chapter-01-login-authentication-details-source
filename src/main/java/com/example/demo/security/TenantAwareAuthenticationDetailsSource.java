package com.example.demo.security;

import com.example.demo.resolver.TenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantAwareAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, TenantAwareAuthenticationDetails> {

    private final TenantResolver tenantResolver;

    @Override
    public TenantAwareAuthenticationDetails buildDetails(HttpServletRequest request) {
        String tenantId = tenantResolver.fromHost(request.getServerName());
        return new TenantAwareAuthenticationDetails(request, tenantId);
    }
}