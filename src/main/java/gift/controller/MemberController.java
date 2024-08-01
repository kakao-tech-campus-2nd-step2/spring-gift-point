package gift.controller;

import gift.dto.LoginRequestDTO;
import gift.dto.RegisterRequestDTO;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "멤버 관리 API", description = "멤버(사용자) 관리를 위한 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "회원 가입합니다.")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        String token = memberService.registerMember(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Authorization", "Bearer " + token)
            .build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 합니다.")
    public ResponseEntity<Void> loginMember(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        String token = memberService.loginMember(loginRequestDTO);
        return ResponseEntity.ok()
            .header("Authorization", "Bearer " + token)
            .build();
    }

}