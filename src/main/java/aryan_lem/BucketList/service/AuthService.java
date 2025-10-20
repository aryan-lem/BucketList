package aryan_lem.BucketList.service;


import aryan_lem.BucketList.model.User;
import aryan_lem.BucketList.security.JwtUtil;
import aryan_lem.BucketList.dto.*;
import aryan_lem.BucketList.repo.RedisService;
import aryan_lem.BucketList.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepo, RedisService redisService, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.redisService = redisService;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    public String register(UserDTO dto) {
        if (userRepo.findByUsername(dto.getUsername()).isPresent())
            throw new RuntimeException("User already exists");
        User user = new User(dto.getId(), dto.getUsername(), encoder.encode(dto.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }

    public TokenResponse login(LoginDTO dto) {
        Optional<User> uOpt = userRepo.findByUsername(dto.getUsername());
        if (uOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        User user = uOpt.get();
        if (!encoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // persist refresh token in Redis so it can be invalidated on logout / validated on refresh
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("refresh", refreshToken);
        tokenMap.put("issuedAt", System.currentTimeMillis());
        redisService.saveTokens(user.getUsername(), tokenMap);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        String username = jwtUtil.extractUsername(refreshToken);

        Optional<Map<String, Object>> stored = redisService.getTokens(username);
        if (stored.isEmpty()) {
            throw new IllegalArgumentException("Refresh token not found / logged out");
        }
        Object storedRefresh = stored.get().get("refresh");
        if (storedRefresh == null || !refreshToken.equals(storedRefresh.toString())) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        String newAccess = jwtUtil.generateAccessToken(username);
        String newRefresh = jwtUtil.generateRefreshToken(username);

        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("refresh", newRefresh);
        tokenMap.put("issuedAt", System.currentTimeMillis());
        redisService.saveTokens(username, tokenMap);

        return new TokenResponse(newAccess, newRefresh);
    }
    public void logout(String username) {
        redisService.deleteTokens(username);
    }
}

