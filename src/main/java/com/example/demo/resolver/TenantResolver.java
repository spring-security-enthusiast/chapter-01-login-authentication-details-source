package com.example.demo.resolver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class TenantResolver {

    private final String baseDomain;

    public TenantResolver(@Value("${app.tenancy.base-domain}") String baseDomain) {
        this.baseDomain = baseDomain.toLowerCase();
    }

    public String fromHost(String serverName) {
        if (serverName == null || serverName.isBlank()) return null;

        String host = serverName.toLowerCase();
        if (host.equals(baseDomain)) return null;

        String suffix = "." + baseDomain;
        if (!host.endsWith(suffix)) return null;

        String prefix = host.substring(0, host.length() - suffix.length()); // "acme"
        if (prefix.isBlank()) return null;

        // if you ever allow "foo.acme", pick rule here
        String[] labels = prefix.split("\\.");
        return labels[labels.length - 1];
    }
}

