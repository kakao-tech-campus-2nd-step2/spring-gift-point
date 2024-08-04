package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.Member;
import gift.dto.MemberDto;
import gift.dto.PointResponseDto;
import gift.response.JwtResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "멤버 API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다.")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody MemberDto memberDto) {
        String newMemberEmail = memberService.registerMember(memberDto, null);
        String jwt = memberService.login(memberDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(jwt));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody MemberDto memberDto) {
        String jwt = memberService.login(memberDto);
        return ResponseEntity.ok().body(new JwtResponse(jwt));
    }

    @GetMapping("/point")
    @Operation(summary = "포인트 조회", description = "사용자의 현재 보유 포인트를 받는다.")
    public ResponseEntity<PointResponseDto> getPoint (@Parameter(hidden = true) @LoginMember Member member) {
        return ResponseEntity.ok(new PointResponseDto(member.getPoints()));
    }

}
