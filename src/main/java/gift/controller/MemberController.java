package gift.controller;

import gift.constant.Constants;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberRequest;
import gift.dto.JoinResponse;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "일반 회원가입")
    @PostMapping("/register")
    public ResponseEntity<JoinResponse> registerMember(@RequestBody MemberRequest requestDto) {
        JoinResponse responseDto = memberService.registerMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "일반 로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.status(loginResponse.getAccess_token() != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .header(Constants.AUTHENTICATE_HEADER, Constants.BEARER)
                .body(loginResponse);
    }
}