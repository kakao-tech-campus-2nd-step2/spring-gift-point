package gift.controller.api;

import gift.dto.request.MemberRequest;
import gift.dto.response.JwtResponse;
import gift.service.MemberService;
import gift.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/members/register")
    public ResponseEntity<JwtResponse> registerMember(@Valid @RequestBody MemberRequest request) {
        Long registeredMemberId = memberService.register(request);
        JwtResponse token = tokenService.generateJwt(registeredMemberId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/members/login")
    public ResponseEntity<JwtResponse> loginMember(@Valid @RequestBody MemberRequest request) {
        Long registeredMemberId = memberService.login(request);
        JwtResponse token = tokenService.generateJwt(registeredMemberId);
        return ResponseEntity.ok(token);
    }
}
