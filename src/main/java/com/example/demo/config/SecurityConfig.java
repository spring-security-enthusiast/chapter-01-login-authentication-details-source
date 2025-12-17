package com.example.demo.config;

import com.example.demo.handler.LoginSuccessHandler;
import com.example.demo.provider.TenantAuthenticationProvider;
import com.example.demo.security.TenantAwareAuthenticationDetailsSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;


@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TenantAwareAuthenticationDetailsSource authenticationDetailsSource;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TenantAuthenticationProvider tenantAuthenticationProvider) {
        http
            .headers(headers -> headers
                 .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
            .authenticationProvider(tenantAuthenticationProvider)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    PathRequest.toH2Console(),
                    PathPatternRequestMatcher.withDefaults().matcher("/error/**"),
                    PathRequest.toStaticResources().atCommonLocations()
                ).permitAll()
                .requestMatchers("/auth/**", "/", "/index", "/index.html", "/go").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login_processing")
                    .authenticationDetailsSource(authenticationDetailsSource) // ◄── Register here
                    .successHandler(loginSuccessHandler)
                    .permitAll()
            )
            .logout(LogoutConfigurer::permitAll);

        return http.build();
    }


}
