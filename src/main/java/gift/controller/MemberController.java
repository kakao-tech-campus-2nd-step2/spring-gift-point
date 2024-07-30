package gift.controller;

import gift.domain.Member;
import gift.domain.Token;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 가입 후 토큰 발행")
    public ResponseEntity<?> register(@Valid @RequestBody Member member) {
        Token token = memberService.register(member);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    @Operation(summary = "회원 로그인", description = "회원 로그인 후 토큰 발행")
    public ResponseEntity<?> login(@Valid @RequestBody Member member) {
        Token token = memberService.login(member);
        return ResponseEntity.ok(token);
    }

}
