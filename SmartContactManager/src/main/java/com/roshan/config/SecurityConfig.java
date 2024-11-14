package com.roshan.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.roshan.service.SecurityCustomUserDetailsService;
@Configuration
@EnableWebMvc
public class SecurityConfig {
    @Autowired
    private OAuthAuthenticationSuccessHandler handler;
    @Autowired
    private SecurityCustomUserDetailsService service;
    // Security filter chain with form login and OAuth2 login
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> {
            // Protect other URLs and require authentication
            authorize.requestMatchers("/user/**").authenticated();
            // Allow login and register pages to be publicly accessible
            authorize.anyRequest().permitAll();
        });
        // Form login configuration
        http.formLogin(formLogin -> {
            formLogin.loginPage("/login").loginProcessingUrl("/authenticate").defaultSuccessUrl("/user/dashboard")
                    .failureForwardUrl("/login?error=true").usernameParameter("email").passwordParameter("password");
        });
        // Logout configuration
        http.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true") // Redirect to login page after
                    // logout
                    .invalidateHttpSession(true) // Invalidate the local session
                    .deleteCookies("JSESSIONID", "OAUTH2_AUTHORIZATION_CODE", "OAUTH2_ACCESS_TOKEN") // Clear OAuth2
                    // cookies
                    .clearAuthentication(true); // Clear the authentication context
        });
        // OAuth2 login configuration
        http.oauth2Login(oauth -> {
            oauth.loginPage("/login"); // Custom login page for OAuth2 login
            oauth.successHandler(handler); // Custom success handler for OAuth2 login

        });
        return http.build();
    }
    // Authentication provider configuration (for in-memory authentication or
    // DB-backed user service)
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(service);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    // Password encoder bean for encoding passwords
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}