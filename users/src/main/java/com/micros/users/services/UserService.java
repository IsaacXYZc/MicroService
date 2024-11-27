package com.micros.users.services;

import com.micros.users.exceptions.UserAlreadyExistsException;
import com.micros.users.models.PasswordChangeRequest;
import com.micros.users.models.UserRegisterRequest;
import com.micros.users.models.UserResponse;
import com.micros.users.models.UserUpdateRequest;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final KeycloakService keycloakService;

    public UserService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }


    public String createUser(UserRegisterRequest userDto) {
        try {
            String userId = keycloakService.createUserInKeycloak(userDto);

            keycloakService.assignRoleToUser(userId, "user");

            return "User created successfully!";
        } catch (UserAlreadyExistsException e) {
            return "User already exists!";
        } catch (Exception e) {
            return "Error creating user, please contact the administrator.";
        }
    }
        ////////////////////////////////////////
    public List<UserResponse> getAllUsers() {
        return keycloakService.getAllUsers();
    }
    // Obtener un usuario por su ID
    public UserResponse getUserById(String id) {
        return keycloakService.getUserById(id);
    }

    // Actualizar la información de un usuario
    public void updateUser(String id, UserUpdateRequest userUpdateRequest) {
        keycloakService.updateUserInKeycloak(id, userUpdateRequest);
    }

    // Eliminar un usuario por su ID
    public void deleteUser(String id) {
        keycloakService.deleteUserInKeycloak(id);
    }

    // Cambiar la contraseña de un usuario
    public void changePassword(String id, PasswordChangeRequest password) {
        keycloakService.changeUserPassword(id, password);
    }


    private String getPreferredUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
            return jwt.getClaimAsString("preferred_username");
        }
        return null;
    }
}
