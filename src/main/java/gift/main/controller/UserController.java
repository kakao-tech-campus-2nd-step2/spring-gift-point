package gift.main.controller;

import gift.main.dto.UserJoinRequest;
import gift.main.dto.UserLoginRequest;
import gift.main.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/join")
    public ResponseEntity<?> joinMember(@Valid @RequestBody UserJoinRequest userJoinRequest, HttpServletResponse response) {
        Map<String, Object> responseBody = new HashMap<>();
        String token = userService.joinUser(userJoinRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(responseBody);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<?> loinMember(@Valid @RequestBody UserLoginRequest userloginDto, HttpServletResponse response) {
        Map<String, Object> responseBody = new HashMap<>();
        String token = userService.loginUser(userloginDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(responseBody);

    }
}
