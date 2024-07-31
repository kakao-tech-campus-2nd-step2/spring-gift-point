package gift.controller;

import gift.dto.KakaoDTO;
import gift.entity.MemberEntity;
import gift.dto.MemberDTO;
import gift.service.JwtUtil;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Tag(name = "회원 가입 및 로그인 API")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Operation(summary = "회원가입", description = "이메일과 비밀번호를 이용하여 회원가입을 진행합니다.")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return new ResponseEntity<>(Collections.singletonMap("status", "SUCCESS"), HttpStatus.OK);
    }

    @Operation(summary = "이메일로 로그인", description = "이메일과 비밀번호를 통해 JWT 토큰을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody MemberDTO memberDTO) {
        MemberEntity authenticatedMember = memberService.authenticateToken(memberDTO);
        String token = jwtUtil.generateToken(authenticatedMember.getEmail(), authenticatedMember.getId(),"");
        Map<String, String> response = Collections.singletonMap("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "카카오 로그인", description = "카카오 액세스 토큰을 통해 JWT 토큰을 반환합니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<?> loginKakaoUser(@RequestBody KakaoDTO kakaoDTO) {
        MemberEntity authenticatedMember = memberService.registerOrLoginKakaoUser(kakaoDTO.getAccess_token());
        String token = jwtUtil.generateToken(authenticatedMember.getEmail(), authenticatedMember.getId(), kakaoDTO.getAccess_token());
        Map<String, String> response = Collections.singletonMap("token", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}