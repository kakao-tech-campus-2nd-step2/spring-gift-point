package gift.api.member.controller;

import gift.api.member.dto.MemberRequest;
import gift.api.member.dto.MyInfoResponse;
import gift.api.member.service.MemberService;
import gift.global.resolver.LoginMember;
import gift.global.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "유효하지 않은 요청")
    })
    public ResponseEntity<Void> register(
        @Parameter(required = true)
        @RequestBody @Valid MemberRequest memberRequest) {

        HttpHeaders responseHeaders = new HttpHeaders();
        String accessToken = memberService.register(memberRequest);
        responseHeaders.set("Authorization", JwtUtil.generateHeaderValue(accessToken));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "일반 로그인")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰"),
        @ApiResponse(responseCode = "403", description = "유효하지 않은 요청")
    })
    public ResponseEntity<Void> login(
        @Parameter(required = true, description = "시스템 토큰")
        @RequestHeader("Authorization") String token,
        @Parameter(required = true, description = "사용자 요청 본문")
        @RequestBody MemberRequest memberRequest) {

        memberService.login(memberRequest, token.split(" ")[1]);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    @Operation(summary = "사용자 정보 확인", description = "잔여 포인트 확인")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<MyInfoResponse> me(
        @Parameter(name = "Authorization", required = true, description = "사용자 액세스 토큰")
        @LoginMember Long id) {

        return ResponseEntity.ok().body(memberService.me(id));
    }
}
