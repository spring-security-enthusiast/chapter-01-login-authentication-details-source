package com.example.demo.handler;

import com.example.demo.security.TenantAwareAuthenticationDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public LoginSuccessHandler() {
        setDefaultTargetUrl("/dashboard");
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        Object detailsObj = authentication.getDetails();

        if (detailsObj instanceof TenantAwareAuthenticationDetails details) {
            log.info(
                    "Login success user={} tenant={} device={} ip={} time={}",
                    authentication.getName(),
                    details.getTenantId(),
                    details.getDeviceType(),
                    details.getRemoteAddress(),
                    details.getLoginAttemptTime()
            );
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
