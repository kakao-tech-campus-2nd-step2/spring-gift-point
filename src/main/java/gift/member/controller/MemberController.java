package gift.member.controller;

import gift.common.auth.JwtUtil;
import gift.common.util.CommonResponse;
import gift.member.dto.LoginRequest;
import gift.member.dto.LoginResponse;
import gift.member.dto.SignUpReqeust;
import gift.member.dto.SignUpResponse;
import gift.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API", description = "회원가입, 로그인 API")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    // 회원가입
    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpReqeust request) {
        String email = request.getEmail();
        String password = request.getPassword();

        memberService.registerMember(email, password);
        String token = memberService.login(email, password);

        SignUpResponse response = new SignUpResponse(email, token);

        return ResponseEntity.ok(new CommonResponse<>(response, "회원 가입 후 토큰 받기 성공", true));
    }

    // 로그인
    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String token = memberService.login(email, password);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email or password"); // 403 Forbidden
        }

        LoginResponse response = new LoginResponse(email, token);

        return ResponseEntity.ok(new CommonResponse<>(response, "로그인 후 토큰 발기 성공", true));
    }

    // 특정 회원의 포인트 조회
    @Operation(summary = "포인트 조회", description = "특정 회원의 포인트를 조회한다.")
    @PostMapping("/point")
    public ResponseEntity<?> getPoint(
            @Parameter(hidden = true) @RequestHeader("Authorization") String authorizationHeader
    ) {
        // 토큰 추출 및 검증
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : null;
        if (token == null || !jwtUtil.isTokenValid(token)) {
            // 401 Unauthorized
            return ResponseEntity.status(401).body(new CommonResponse<>(null, "Invalid or missing token", false));
        }

        String memberEmail = jwtUtil.extractEmail(token);
        Long point = memberService.getPoint(memberEmail);

        return ResponseEntity.ok(new CommonResponse<>(point, "포인트 조회 성공", true));
    }
}
