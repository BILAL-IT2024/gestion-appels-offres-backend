package net.bilal.appeldoffresbackend.web;

import lombok.RequiredArgsConstructor;
import net.bilal.appeldoffresbackend.dtos.UserDTO;
import net.bilal.appeldoffresbackend.entities.AppUser;
import net.bilal.appeldoffresbackend.repositories.AppUserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public void deleteUser(@PathVariable Long id) {

        appUserRepository.deleteById(id);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public AppUser updateRole(

            @PathVariable Long id,

            @RequestParam String role
    ) {

        AppUser user =
                appUserRepository.findById(id)
                        .orElseThrow();

        user.setRole(role);

        return appUserRepository.save(user);
    }
}
