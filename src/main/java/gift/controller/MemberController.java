package gift.controller;

import gift.dto.*;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 계정 관련 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "회원가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "양식에 맞지 않는 요청")
    })
    @PostMapping("/signup")
    public ResponseEntity<MemberRegisterResponseDto> signUp(@RequestBody MemberRegisterRequestDto request) {
        return ResponseEntity.ok(memberService.signUpMember(request));
    }

    @Operation(summary = "로그인 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "비밀번호 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody MemberRequestDto request) {
        String token = memberService.loginMember(request);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
