package com.micros.users.services;

import com.micros.users.exceptions.UserAlreadyExistsException;
import com.micros.users.keycloak.KeycloakProvider;
import com.micros.users.models.PasswordChangeRequest;
import com.micros.users.models.UserRegisterRequest;
import com.micros.users.models.UserResponse;
import com.micros.users.models.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final KeycloakProvider keycloakProvider;

    public String createUserInKeycloak(UserRegisterRequest userDto) {
        UsersResource usersResource = keycloakProvider.getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setUsername(userDto.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        if (response.getStatus() == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentials = new CredentialRepresentation();
            credentials.setTemporary(false);
            credentials.setType(CredentialRepresentation.PASSWORD);
            credentials.setValue(userDto.getPassword());

            usersResource.get(userId).resetPassword(credentials);
            return userId;
        } else if (response.getStatus() == 409) {
            throw new UserAlreadyExistsException("User already exists in Keycloak.");
        } else {
            throw new RuntimeException("Error creating user in Keycloak.");
        }
    }

    public List<UserResponse> getAllUsers() {
        UsersResource usersResource = keycloakProvider.getUserResource();

        // Obtener la lista de usuarios desde Keycloak
        List<UserRepresentation> userRepresentations = usersResource.list();

        // Convertir UserRepresentation a UserResponse
        return userRepresentations.stream()
                .map(this::toUserResponse)
                .toList();
    }

    //////////////////////////////////////
    // Obtener un usuario por su ID
    public UserResponse getUserById(String id) {
        UsersResource usersResource = keycloakProvider.getUserResource();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        return toUserResponse(userRepresentation);
    }

    // Actualizar un usuario
    public void updateUserInKeycloak(String id, UserUpdateRequest userUpdateRequest) {
        UsersResource usersResource = keycloakProvider.getUserResource();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();

        // Actualiza los campos necesarios
        if (userUpdateRequest.getEmail() != null) {
            userRepresentation.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getUsername() != null) {
            userRepresentation.setUsername(userUpdateRequest.getUsername());
        }

        usersResource.get(id).update(userRepresentation);
    }

    // Eliminar un usuario
    public void deleteUserInKeycloak(String id) {
        UsersResource usersResource = keycloakProvider.getUserResource();
        usersResource.delete(id);
    }

    // Cambiar la contraseña de un usuario
    public void changeUserPassword(String id, PasswordChangeRequest password) {
        System.out.println(password.getPassword());
        UsersResource usersResource = keycloakProvider.getUserResource();

        CredentialRepresentation credentials = new CredentialRepresentation();
        credentials.setTemporary(false);
        credentials.setType(CredentialRepresentation.PASSWORD);
        credentials.setValue(password.getPassword());

        usersResource.get(id).resetPassword(credentials);
    }

    // Método de conversión
    private UserResponse toUserResponse(UserRepresentation userRepresentation) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userRepresentation.getId());
        userResponse.setUsername(userRepresentation.getUsername());
        userResponse.setEmail(userRepresentation.getEmail());
        return userResponse;
    }

    public void assignRoleToUser(String userId, String roleName) {
        RealmResource realmResource = keycloakProvider.getRealmResource();
        RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
        realmResource.users().get(userId).roles().realmLevel().add(List.of(role));
    }
}
