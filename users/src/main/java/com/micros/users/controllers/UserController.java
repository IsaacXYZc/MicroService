package com.micros.users.controllers;

import com.micros.users.models.*;
import com.micros.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registrar un nuevo usuario
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody UserRegisterRequest user) {
        userService.createUser(user);
        return "Usuario creado exitosamente";
    }

    // Obtener todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Obtener un usuario por su ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Actualizar la información de un usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin') or #id == authentication.principal.username")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }

    // Eliminar un usuario por su ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Usuario eliminado exitosamente");
    }

    // Cambiar la contraseña de un usuario
    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasRole('admin') or #id == authentication.principal.username")
    public ResponseEntity<String> changePassword(@PathVariable String id, @RequestBody PasswordChangeRequest password) {
        System.out.println("contraseña:"+password);
        userService.changePassword(id, password);
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }
}
