package com.example.demo.service.impl;

import com.example.demo.repository.TenantEntityRepository;
import com.example.demo.security.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TenantEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        String tenantId = Optional.ofNullable(TenantContextHolder.getTenantId()).orElse("");
        if (tenantId.isEmpty()) {
            throw new UsernameNotFoundException("Tenant not resolved");
        }

        return userRepository
                .findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
