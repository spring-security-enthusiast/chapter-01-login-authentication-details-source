package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TenantUser {

    private String tenantId;

    private String username;

    private String role;

    private String loginUrl;

    public TenantUser(String tenantId, String username, String role) {
        this.tenantId = tenantId;
        this.username = username;
        this.role = role;
    };
}


