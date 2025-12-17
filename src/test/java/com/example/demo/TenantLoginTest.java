package com.example.demo;

import com.example.demo.security.TenantAwareAuthenticationDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TenantLoginTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void acme_john_canLogin_andDetailsCaptured() throws Exception {
        // 1) login
        MvcResult loginResult = mockMvc.perform(post("/auth/login_processing")
                        .with(csrf())
                        .header("Host", "acme.localhost")
                        .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 14_6...)")
                        .param("username", "john")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("john"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        // 2) hit a protected page with same session (proves session auth works)
        mockMvc.perform(get("/dashboard")
                        .header("Host", "acme.localhost")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(authenticated());

        // 3) assert SecurityContext stored in session contains your details (MOST RELIABLE)
        SecurityContext context = (SecurityContext) session.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
        );
        assertThat(context).isNotNull();

        Authentication auth = context.getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("john");

        assertThat(auth.getDetails()).isInstanceOf(TenantAwareAuthenticationDetails.class);
        TenantAwareAuthenticationDetails details = (TenantAwareAuthenticationDetails) auth.getDetails();

        assertThat(details.getTenantId()).isEqualTo("acme");
        assertThat(details.getDeviceType()).isEqualTo("MOBILE");
        assertThat(details.getUserAgent()).contains("iPhone");
        assertThat(details.getLoginAttemptTime()).isNotNull();
        assertThat(details.getRemoteAddress()).isNotBlank();
    }

    @Test
    void globex_john_cannotLogin() throws Exception {
        mockMvc.perform(post("/auth/login_processing")
                        .with(csrf())
                        .header("Host", "globex.localhost")
                        .param("username", "john")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
    }

    @Test
    void globex_bob_canLogin_andDetailsCaptured() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/auth/login_processing")
                        .with(csrf())
                        .header("Host", "globex.localhost")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .param("username", "bob")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(authenticated().withUsername("bob"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        // 2) hit a protected page with same session (proves session auth works)
        mockMvc.perform(get("/dashboard")
                        .header("Host", "globex.localhost")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(authenticated());

        // 3) assert SecurityContext stored in session contains your details (MOST RELIABLE)
        SecurityContext context = (SecurityContext) session.getAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
        );
        assertThat(context).isNotNull();

        Authentication auth = context.getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("bob");

        assertThat(auth.getDetails()).isInstanceOf(TenantAwareAuthenticationDetails.class);
        TenantAwareAuthenticationDetails details = (TenantAwareAuthenticationDetails) auth.getDetails();

        assertThat(details.getTenantId()).isEqualTo("globex");
        assertThat(details.getDeviceType()).isEqualTo("DESKTOP");
        assertThat(details.getUserAgent()).contains("Windows");
        assertThat(details.getLoginAttemptTime()).isNotNull();
        assertThat(details.getRemoteAddress()).isNotBlank();
    }

    @Test
    void acme_bob_cannotLogin() throws Exception {
        mockMvc.perform(post("/auth/login_processing")
                        .with(csrf())
                        .header("Host", "acme.localhost")
                        .param("username", "bob")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
    }



}
