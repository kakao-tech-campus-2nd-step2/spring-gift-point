package gift.controller.user;

import gift.dto.user.UserRequest;
import gift.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController implements UserSpecification {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest.Check userRequest) {
        String token = userService.login(userRequest);
        return ResponseEntity.ok(Map.of("accessToken", token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequest.Create userRequest) {
        userService.register(userRequest);
        return ResponseEntity.ok("회원가입을 성공하였습니다!");
    }
}