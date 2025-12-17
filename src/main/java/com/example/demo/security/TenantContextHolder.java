package com.example.demo.security;

public final class TenantContextHolder {

    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    private TenantContextHolder() {}

    public static void setTenantId(String tenantId) {
        CURRENT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}