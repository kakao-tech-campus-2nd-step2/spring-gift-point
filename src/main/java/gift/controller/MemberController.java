package gift.controller;

import gift.constant.Constants;
import gift.dto.LoginRequest;
import gift.dto.LoginResponse;
import gift.dto.MemberRequest;
import gift.dto.MemberResponse;
import gift.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;

@RestController
@RequestMapping("api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberResponse> registerMember(@RequestBody MemberRequest requestDto) {
        MemberResponse responseDto = memberService.registerMember(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.status(loginResponse.getToken() != null ? HttpStatus.OK : HttpStatus.UNAUTHORIZED)
                .header(Constants.AUTHENTICATE_HEADER, Constants.BEARER)
                .body(loginResponse);
    }
}