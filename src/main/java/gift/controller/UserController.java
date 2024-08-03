package gift.controller;

import gift.dto.TokenResponse;
import gift.model.User;
import gift.service.UserService;
import gift.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody User user) {
        try {
            userService.createUser(user);
            String token = jwtTokenProvider.generateToken(user.getEmail());
            return new ResponseEntity<>(new TokenResponse(token), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody User user) {
        try {
            User savedUser = userService.loadOneUser(user.getEmail());
            if (savedUser != null && savedUser.getPassword().equals(user.getPassword())) {
                String token = jwtTokenProvider.generateToken(savedUser.getEmail());
                return new ResponseEntity<>(new TokenResponse(token), HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}