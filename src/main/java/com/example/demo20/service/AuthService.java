package com.example.demo20.service;

import com.example.demo20.dto.AuthResponse;
import com.example.demo20.dto.LoginRequest;
import com.example.demo20.dto.RegisterRequest;
import com.example.demo20.model.Role;
import com.example.demo20.model.User;
import com.example.demo20.repository.RoleRepository;
import com.example.demo20.repository.UserRepository;
import com.example.demo20.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection bắt buộc để Spring inject
    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthResponse(null, null, "❌ Username already exists!");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRoles(Set.of(userRole));
        userRepository.save(newUser);

        String token = jwtService.generateToken(newUser.getUsername(), Set.of(userRole.getName()));

        return new AuthResponse(token, newUser.getUsername(), userRole.getName());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, "❌ Invalid password!");
        }

        String roleName = user.getRoles().stream()
                .findFirst()
                .map(Role::getName)
                .orElse("ROLE_USER");

        String token = jwtService.generateToken(user.getUsername(), Set.of(roleName));

        return new AuthResponse(token, user.getUsername(), roleName);
    }
}
