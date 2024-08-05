package gift.controller.user;

import gift.dto.user.LoginResponse;
import gift.dto.user.UserRequest;
import gift.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class UserController implements UserSpecification {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse.Info> login(@RequestBody UserRequest.Check userRequest) {
        String token = userService.login(userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        LoginResponse.Info userResponse = userService.getLoginInfo(userRequest.email());

        return ResponseEntity.ok().headers(headers).body(userResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest.Create userRequest) {
        userService.register(userRequest);
        return ResponseEntity.ok("회원가입을 성공하였습니다!");
    }
}