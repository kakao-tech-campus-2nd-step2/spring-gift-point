package gift.controller.signup;

import gift.DTO.member.SignupRequest;
import gift.DTO.member.SignupResponse;
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
@RequestMapping("/signup")
public class SignupController {

    private final MemberService memberService;

    @Autowired
    public SignupController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SignupResponse> signup(
            @RequestBody
            @Valid SignupRequest signupRequest
    ) {
        SignupResponse signupResponse = memberService.registerMember(signupRequest);
        return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
    }
}