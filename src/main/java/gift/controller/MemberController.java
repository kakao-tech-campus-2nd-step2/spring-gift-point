package gift.controller;

import gift.dto.LoginResponseDTO;
import gift.dto.MemberRequestDTO;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody MemberRequestDTO memberRequestDTO) {
        try {
            LoginResponseDTO loginResponse = memberService.register(memberRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 사용자 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody MemberRequestDTO memberRequestDTO) {
        try {
            LoginResponseDTO response = memberService.authenticate(memberRequestDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

