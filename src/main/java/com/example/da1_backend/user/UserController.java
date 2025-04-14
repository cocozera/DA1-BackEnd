package com.example.da1_backend.user;

import com.example.da1_backend.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(@RequestParam("userId") Long userId) {
        UserDTO userDTO = userService.getCurrentUser(userId);
        return ResponseEntity.ok(userDTO);
    }

}
