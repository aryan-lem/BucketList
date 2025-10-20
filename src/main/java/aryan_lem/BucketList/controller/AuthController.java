package aryan_lem.BucketList.controller;

import aryan_lem.BucketList.dto.*;
import aryan_lem.BucketList.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public String register(@RequestBody UserDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginDTO dto) {
        return authService.login(dto);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestParam String token) {
        return authService.refresh(token);
    }
}

