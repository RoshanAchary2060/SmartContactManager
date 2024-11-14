package com.roshan.config;
import java.io.IOException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // TODO Auto-generated method stub
        if (exception instanceof BadCredentialsException) {
            // Handle bad credentials
            request.setAttribute("error", "Invalid username or password");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
//
}