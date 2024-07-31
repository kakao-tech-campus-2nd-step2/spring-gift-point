package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<MemberResponseDto> register(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        MemberResponseDto response = memberService.register(memberRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "기존 회원을 로그인 합니다.")
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        MemberResponseDto response = memberService.login(memberRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());
        return ResponseEntity.ok().headers(headers).body(response);
    }
}
