package com.micros.users.services;

import com.micros.users.models.UserEntity;
import com.micros.users.models.UserRequest;
import com.micros.users.models.UserResponse;
import com.micros.users.repositories.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserRequest user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalArgumentException("Nombre de Usuario ya registrado");
        }
        userRepository.save(UserEntity.builder()
                        .username(user.getUsername())
                        .password(passwordEncoder.encode(user.getPassword()))
                .build());
    }

    public UserResponse login(UserRequest user) throws AccessDeniedException {
        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AccessDeniedException("Nombre de usuario inexistente"));
        if(passwordEncoder.matches(user.getPassword(), userEntity.getPassword())) {
            return UserResponse.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .build();
        }
        throw new AccessDeniedException("Contraseña incorrecta");
    }

    public UserResponse getUser(Long id) {
        return UserResponse.builder()
                .id(id)
                .username(userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Usuario con es id no existe."))
                        .getUsername())
                .build();
    }
}
