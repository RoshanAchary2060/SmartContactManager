package com.roshan.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.roshan.repo.IUserRepo;
@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // load the user
        return repo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email id"));

    }
}