package com.rougeux.auction.service.security;

import com.rougeux.auction.configuration.security.contract.UserPrincipal;
import com.rougeux.auction.dal.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao repository;

    @Override
    @Cacheable(value = "principal", key = "#username")
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserPrincipal.builder()
                .user(repository.findByUsername(username)
                        .orElseThrow(() -> new BadCredentialsException("login.error.invalidCredentials")))
                .build();
    }
}
