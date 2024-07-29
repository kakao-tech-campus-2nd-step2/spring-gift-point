package gift.user;


import gift.jwt.JWTService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    public UserController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public Token loginUser(@RequestBody @Valid LoginDTO loginDTO) throws Exception {
        if (!userService.validateUser(loginDTO)) {
            throw new IllegalArgumentException("이메일이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        }
        User user = userService.getUserByEmail(loginDTO.getEmail());
        String accessToken = jwtService.generateAccessToken(user);

        return new Token(accessToken);
    }

    @PostMapping("/signup")
    public Token signupUser(@RequestBody @Valid UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        String accessToken = jwtService.generateAccessToken(user);
        return new Token(accessToken);
    }


}
