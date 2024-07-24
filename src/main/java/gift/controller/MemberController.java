package gift.controller;

import gift.dto.LoginMemberToken;
import gift.dto.MemberRequest;
import gift.model.MemberRole;
import gift.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping
    public ResponseEntity<Void> register(@RequestBody @Valid MemberRequest memberRequest) {
        if (memberRequest.getRole() == null) {
            memberRequest.setRole(MemberRole.COMMON_MEMBER);
        }
        memberService.register(memberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<LoginMemberToken> login(@RequestParam("email") @NotBlank String email,
        @RequestParam("password") @NotBlank String password) {
        return ResponseEntity.ok(memberService.login(new MemberRequest(email, password, null)));
    }
}
