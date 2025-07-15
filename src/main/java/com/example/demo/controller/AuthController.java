package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.jwt.JwtResponse;
import com.example.demo.repository.UserRepository;
import com.example.demo.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        String jwt = jwtUtils.generateToken(user.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        User newUser = new User(
                null,
                user.getUsername(),
                encoder.encode(user.getPassword())
        );
        userRepository.save(newUser);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully!");
    }
}