package gift.controller;

import gift.dto.MemberDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Member", description = "회원 관련 api")
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 가입을합니다.")
    public ResponseEntity<String> register(@RequestBody @Valid MemberDto memberDto) {
        String token = memberService.register(memberDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "기존 회원을 로그인 합니다.")
    public ResponseEntity<String> login(@RequestBody @Valid MemberDto memberDto) {
        String token = memberService.login(memberDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return ResponseEntity.ok().headers(headers).body("{\"token\": \"" + token + "\"}");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 합니다.")
    public ResponseEntity<String> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = authHeader.substring(7);
        memberService.logout(token);
        return ResponseEntity.ok().body("{\"message\": \"로그아웃 되었습니다.\"}");
    }
}
