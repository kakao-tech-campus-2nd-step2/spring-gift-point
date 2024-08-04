package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.model.CurrentMember;
import gift.model.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/points")
    @Operation(summary = "포인트 조회", description = "회원의 포인트를 조회합니다.")
    public ResponseEntity<Map<String, Integer>> getPoints(@CurrentMember Member member) {
        int points = memberService.getMemberPoints(member.getEmail());
        Map<String, Integer> response = new HashMap<>();
        response.put("points", points);
        return ResponseEntity.ok(response);
    }
}
