package gift.controller;

import gift.dto.TokenDTO;
import gift.dto.UserDTO;
import gift.jwtutil.JwtUtil;
import gift.service.AuthService;
import gift.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Validated
@Controller
@RequestMapping("/members/register")
public class RegisterController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public RegisterController(AuthService authService, UserService userService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public String registerPage() {
        return "register";
    }

    @PostMapping
    public ResponseEntity<?> registUser(@Valid @RequestBody UserDTO userDTO) {
        authService.redundantUser("regist", userDTO);

        userService.createUser(userDTO);
        TokenDTO token = jwtUtil.makeToken(userDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}
