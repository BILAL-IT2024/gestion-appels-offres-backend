package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.dtos.UserDTO;
import net.bilal.appeldoffresbackend.entities.AppUser;
import net.bilal.appeldoffresbackend.repositories.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final AppUserRepository appUserRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> getAllUsers() {

        return appUserRepository.findAll()
                .stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .toList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(
            @PathVariable Long id,
            Authentication authentication
    ) {

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));

        if (user.getUsername().equals(authentication.getName())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vous ne pouvez pas supprimer votre propre compte"
            );
        }

        appUserRepository.delete(user);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDTO updateRole(
            @PathVariable Long id,
            @RequestParam String role,
            Authentication authentication
    ) {

        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur introuvable"
                ));

        if (user.getUsername().equals(authentication.getName())
                && !"ADMIN".equalsIgnoreCase(role)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vous ne pouvez pas retirer votre propre rôle ADMIN"
            );
        }

        user.setRole(role.toUpperCase());

        AppUser saved = appUserRepository.save(user);

        return new UserDTO(
                saved.getId(),
                saved.getUsername(),
                saved.getRole()
        );
    }
}
