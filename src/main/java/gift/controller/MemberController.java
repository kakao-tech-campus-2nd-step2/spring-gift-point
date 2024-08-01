package gift.controller;


import gift.dto.memberDto.MemberDto;
import gift.dto.memberDto.MemberResponseDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequestMapping("/members")
@Controller
@Tag(name = "Member Management", description = "Member Management API")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "새로운 멤버 회원가입", description = "회원가입 할 때 사용하는 API")
    public ResponseEntity<MemberResponseDto> registerNewMember(@RequestBody MemberDto memberDto) {
        MemberResponseDto memberResponseDto = memberService.registerNewMember(memberDto);
        return ResponseEntity.ok().body(memberResponseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "기존 멤버 로그인", description = "로그인 할 때 사용하는 API")
    public ResponseEntity<?> loginMember(@RequestBody MemberDto memberDto) {
        String token = memberService.loginMember(memberDto);
        return ResponseEntity.ok().body(Collections.singletonMap("token", token));
    }

    @GetMapping("/register")
    @Operation(summary = "회원가입 페이지로 이동", description = "회원가입 페이지로 이동할 때 사용하는 API")
    public String moveToRegister() {
        return "registerMember";
    }

    @GetMapping("/login")
    @Operation(summary = "로그인 페이지로 이동", description = "로그인 페이지로 이동할 때 사용하는 API")
    public String moveToLogin() {
        return "loginMember";
    }
}

