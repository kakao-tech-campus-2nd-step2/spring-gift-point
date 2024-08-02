package gift.controller.login;

import gift.DTO.member.LoginRequest;
import gift.DTO.member.LoginResponse;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/login")
public class LoginController {

    private final MemberService memberService;

    @Autowired
    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<String> login(
            @RequestBody
            @Valid LoginRequest loginRequest
    ) {
        LoginResponse loginResponse = memberService.loginMember(loginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse.getToken());
    }
}