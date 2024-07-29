package gift.controller;

import gift.dto.MemberDto;
import gift.response.JwtResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@Tag(name = "Member", description = "멤버 API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원을 등록합니다.")
    public ResponseEntity<String> register(@Valid @RequestBody MemberDto memberDto) {
        String newMemberEmail = memberService.registerMember(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("새 멤버의 이메일: " + newMemberEmail);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원을 로그인합니다.")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody MemberDto memberDto) {
        String jwt = memberService.login(memberDto);
        return ResponseEntity.ok()
            .body(new JwtResponse(jwt));
    }
}
