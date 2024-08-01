package gift.controller;


import gift.dto.user.LoginDTO;
import gift.dto.user.SignUpDTO;

import gift.dto.user.UserResponseDTO;
import gift.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signUp() {
        return "user/signup";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/members/register")
    @ResponseBody
    public UserResponseDTO SignUp(@RequestBody SignUpDTO signUpDTO) {
        return userService.signUp(signUpDTO);
    }

    @GetMapping("/signin")
    public String signIn() {
        return "user/signin";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/members/login")
    @ResponseBody
    public UserResponseDTO signIn(@RequestBody LoginDTO loginDTO) {
        return userService.signIn(loginDTO);
    }

    @PostMapping("/api/kakao/login")
    @ResponseBody
    public UserResponseDTO kakakLogin() {
        return userService.kakaoLogin();
    }

}
