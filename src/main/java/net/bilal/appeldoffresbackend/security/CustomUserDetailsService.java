package net.bilal.appeldoffresbackend.security;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.entities.AppUser;
import net.bilal.appeldoffresbackend.repositories.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = appUserRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Utilisateur introuvable"
                        )
                );

        return new org.springframework.security.core.userdetails.User(
                appUser.getUsername(),
                appUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + appUser.getRole()

                        )
                )
        );
    }
}