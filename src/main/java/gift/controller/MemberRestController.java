package gift.controller;

import gift.dto.response.TokenResponse;
import gift.dto.request.MemberRequest;
import gift.service.JwtUtil;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberRestController {
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public MemberRestController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "새로운 회원을 등록합니다")
    @PostMapping("/register")
    public ResponseEntity<Void> registerMember(@RequestBody MemberRequest request){
        memberService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인하고 액세스 토큰(jwt 토큰)을 반환합니다")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> giveAccessToken(@RequestBody MemberRequest request) {
        memberService.checkMemberExistsByEmailAndPassword(request.email(),request.password());
        String token = jwtUtil.generateToken(request.email());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(new TokenResponse(token));
    }
}
