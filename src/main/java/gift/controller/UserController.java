package gift.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.user.LoginDTO;
import gift.dto.user.SignUpDTO;
import gift.dto.user.Token;

import gift.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/signup")
    public String signUp() {
        return "user/signup";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/signup")
    @ResponseBody
    public String SignUp(@RequestBody SignUpDTO signUpDTO) {
        userService.signUp(signUpDTO);
        return "회원가입 성공";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "user/signin";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/signin")
    @ResponseBody
    public Token signIn(@RequestBody LoginDTO loginDTO) {
        return userService.signIn(loginDTO);
    }

    @PostMapping("/api/kakao/login")
    @ResponseBody
    public Token kakakLogin() {
        return userService.kakaoLogin();
    }

}
