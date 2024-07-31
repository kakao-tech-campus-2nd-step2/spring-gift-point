package gift.domain.member.controller;

import gift.auth.jwt.JwtToken;
import gift.domain.member.dto.MemberRequest;
import gift.domain.member.dto.MemberLoginRequest;
import gift.domain.member.service.MemberService;
import gift.exception.DuplicateEmailException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "회원 API")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "회원 가입", description = "회원 정보를 등록합니다.")
    public ResponseEntity<JwtToken> create(
        @Parameter(description = "회원 등록 정보", required = true)
        @RequestBody @Valid MemberRequest memberRequest
    ) {
        try {
            JwtToken jwtToken = memberService.signUp(memberRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtToken);
        } catch (DuplicateKeyException e) {
            throw new DuplicateEmailException("error.duplicate.key.email");
        }
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 정보를 통해 로그인합니다.")
    public ResponseEntity<JwtToken> login(
        @Parameter(description = "회원 로그인 정보", required = true)
        @RequestBody @Valid MemberLoginRequest memberLoginRequest
    ) {
        JwtToken jwtToken = memberService.login(memberLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
