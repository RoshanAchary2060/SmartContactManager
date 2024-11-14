package com.roshan.config;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.roshan.entity.Providers;
import com.roshan.entity.Users;
import com.roshan.helper.AppConstants;
import com.roshan.repo.IUserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private IUserRepo repo;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // identify the Provider
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = token.getAuthorizedClientRegistrationId();
        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        oauthUser.getAttributes().forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
        Users user = new Users();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");
        if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String email = oauthUser.getAttribute("email") != null ?
                    oauthUser.getAttribute("email").toString() : oauthUser.getAttribute("login").toString()+"@gmail.com" ;
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setProviderUserId(providerUserId);
            user.setName(name);
            user.setProvider(Providers.GITHUB);
            user.setAbout("This account is created using github");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
            user.setName(oauthUser.getAttribute("name").toString());
            user.setProviderUserId(oauthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using google");
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("facebook")) {
        } else {
        }
        Users user2 = repo.findByEmail(user.getEmail()).orElse(null);
        if(user2 == null) {
            repo.save(user);
            System.out.println("User saved");
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}