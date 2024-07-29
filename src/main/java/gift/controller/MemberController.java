package gift.controller;

import gift.dto.member.MemberLoginRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(
        @Valid @RequestBody MemberRegisterRequest memberRegisterRequest
    ) {
        MemberResponse registeredMember = memberService.registerMember(memberRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredMember);
    }

    @Operation(summary = "회원 로그인", description = "회원 로그인을 처리합니다.")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
        @Valid @RequestBody MemberLoginRequest memberLoginRequest
    ) {
        MemberResponse loggedInMember = memberService.loginMember(memberLoginRequest);
        return ResponseEntity.ok(loggedInMember);
    }
}
