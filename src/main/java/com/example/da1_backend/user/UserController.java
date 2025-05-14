package com.example.da1_backend.user;

import com.example.da1_backend.user.dto.UserDTO;
import com.example.da1_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        UserDTO userDTO = userService.getCurrentUser(userId);
        return ResponseEntity.ok(userDTO);
    }

}
