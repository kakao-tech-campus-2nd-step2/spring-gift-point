package gift.controller;

import gift.auth.JwtTokenUtil;
import gift.dto.DomainResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member API", description = "APIs related to member operations")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Operation(summary = "회원 가입", description = "새 회원을 등록하고 토큰을 받는다.")
    @PostMapping("/register")
    public ResponseEntity<DomainResponse> register(@Valid @RequestBody Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), "Validation errors");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("errors", bindingResult.getAllErrors().toString())), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        try {
            memberService.register(member.getEmail(), member.getPassword());
            HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "User registered successfully");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("message", "User registered successfully")), HttpStatus.OK), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("error", e.getMessage())), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "로그인", description = "회원을 인증하고 토큰을 받는다.")
    @PostMapping("/login")
    public ResponseEntity<DomainResponse> login(@RequestBody Member member) {
        Member authenticatedMember = memberService.authenticate(member.getEmail(), member.getPassword());
        if (authenticatedMember != null) {
            String token = JwtTokenUtil.generateToken(authenticatedMember);
            HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Login successful");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("token", token)), HttpStatus.OK), HttpStatus.OK);
        } else {
            HttpResult httpResult = new HttpResult(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("error", "Invalid email or password")), HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
        }
    }
}