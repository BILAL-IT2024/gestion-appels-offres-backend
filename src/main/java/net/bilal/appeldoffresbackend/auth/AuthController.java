package net.bilal.appeldoffresbackend.auth;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import net.bilal.appeldoffresbackend.entities.AppUser;
import net.bilal.appeldoffresbackend.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token =
                jwtService.generateToken(
                        request.getUsername()
                );

        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody RegisterRequest request) {

        AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        return appUserRepository.save(appUser);
    }
}