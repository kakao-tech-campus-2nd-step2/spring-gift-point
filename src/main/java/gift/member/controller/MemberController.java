package gift.member.controller;

import gift.auth.dto.LoginReqDto;
import gift.auth.dto.LoginResDto;
import gift.auth.service.AuthService;
import gift.auth.token.AuthToken;
import gift.member.dto.MemberReqDto;
import gift.member.dto.MemberResDto;
import gift.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API", description = "회원 정보 관리 API")
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    public MemberController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원을 가입하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
            })
    public ResponseEntity<AuthToken> register(@RequestBody MemberReqDto memberReqDto) {
        return ResponseEntity.ok(memberService.register(memberReqDto));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공"),
            })
    public ResponseEntity<LoginResDto> login(@RequestBody LoginReqDto loginReqDto) {
        LoginResDto response = authService.login(loginReqDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "회원 목록 조회", description = "모든 회원 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 목록 조회 성공"),
            })
    public ResponseEntity<List<MemberResDto>> getMembers() {
        return ResponseEntity.ok(memberService.getMembers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 조회", description = "회원을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공"),
            })
    public ResponseEntity<MemberResDto> getMember(@PathVariable("id") Long memberId) {
        return ResponseEntity.ok(memberService.getMember(memberId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 수정", description = "회원 정보를 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 수정 성공"),
            })
    public ResponseEntity<String> updateMember(@PathVariable("id") Long memberId, @RequestBody MemberReqDto memberReqDto) {
        memberService.updateMember(memberId, memberReqDto);
        return ResponseEntity.ok("회원정보 수정 성공");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "회원을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 삭제 성공"),
            })
    public ResponseEntity<String> deleteMember(@PathVariable("id") Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.ok("회원 삭제 성공");
    }
}
