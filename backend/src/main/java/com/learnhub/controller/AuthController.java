package com.learnhub.controller;

import com.learnhub.entity.Role;
import com.learnhub.entity.User;
import com.learnhub.repository.UserRepository;
import com.learnhub.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public record RegisterRequest(@NotBlank String name, @NotBlank @Email String email, @NotBlank @jakarta.validation.constraints.Size(min = 8, max = 100) String password, String role) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String normalizedEmail = req.email().trim().toLowerCase(java.util.Locale.ROOT);
        if (userRepository.existsByEmail(normalizedEmail)) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email already registered"));
        }
        Role role = Role.STUDENT;
        if (req.role() != null && !req.role().isBlank()) {
            try { role = Role.valueOf(req.role().trim().toUpperCase(java.util.Locale.ROOT)); }
            catch (IllegalArgumentException ex) { return ResponseEntity.badRequest().body(Map.of("message", "Invalid role")); }
            if (role == Role.ADMIN) role = Role.STUDENT;
        }
        User user = User.builder()
                .name(req.name().trim())
                .email(normalizedEmail)
                .password(encoder.encode(req.password()))
                .role(role)
                .bio("New learner on Learn Hub")
                .build();
        userRepository.save(user);
        return ResponseEntity.ok(authPayload(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        String normalizedEmail = req.email().trim().toLowerCase(java.util.Locale.ROOT);
        User user = userRepository.findByEmail(normalizedEmail).orElse(null);
        if (user == null || !encoder.matches(req.password(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
        }
        return ResponseEntity.ok(authPayload(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userView(user));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User user, @RequestBody Map<String, String> body) {
        if (body.get("name") != null) user.setName(body.get("name"));
        if (body.get("bio") != null) user.setBio(body.get("bio"));
        userRepository.save(user);
        return ResponseEntity.ok(userView(user));
    }

    private Map<String, Object> authPayload(User user) {
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return Map.of("token", token, "user", userView(user));
    }

    private Map<String, Object> userView(User user) {
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole().name(),
                "bio", user.getBio() == null ? "" : user.getBio(),
                "avatarColor", user.getAvatarColor() == null ? "#2D6A4F" : user.getAvatarColor()
        );
    }
}
