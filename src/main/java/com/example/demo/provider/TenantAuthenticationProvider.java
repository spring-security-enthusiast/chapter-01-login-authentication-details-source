package com.example.demo.provider;


import com.example.demo.entity.TenantEntity;
import com.example.demo.repository.TenantEntityRepository;
import com.example.demo.security.TenantAwareAuthenticationDetails;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TenantAuthenticationProvider implements AuthenticationProvider {

    private final TenantEntityRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();

        String tenantId = Optional.ofNullable(authentication.getDetails())
                .filter(TenantAwareAuthenticationDetails.class::isInstance)
                .map(TenantAwareAuthenticationDetails.class::cast)
                .map(TenantAwareAuthenticationDetails::getTenantId)
                .filter(id -> !id.isBlank())
                .orElseThrow(() -> new BadCredentialsException("Tenant not resolved"));

        TenantEntity user = repo
                .findByTenantIdAndUsername(tenantId, username)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }

        // authenticated token
        return new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
