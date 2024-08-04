package gift.controller;

import gift.auth.Token;
import gift.dto.JoinMemberDto;
import gift.dto.LoginRequestDto;
import gift.service.MemberService;
import gift.vo.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "회원 관리", description = "회원 로그인 및 회원가입과 관련된 API들을 제공합니다.")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    /**
     *
     * @param loginRequestDto LoginDto {email, password}
     * @return JSON { "token" : ""}
     */
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "로그인하여 accessToken을 제공 받는 API입니다."
    )
    @Parameter(name = "loginDto", description = "로그인에 필요한 이메일과 비밀번호를 포함하는 DTO", required = true)
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        Member member = loginRequestDto.toUser();
        Token token = service.login(member);

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    @Operation(
            summary = "회원가입",
            description = "회원가입을 수행하고 access token을 받는 API입니다."
    )
    @Parameter(name = "memberDto", description = "회원가입에 필요한 회원 정보를 포함하는 DTO", required = true)
    public ResponseEntity<Token> join(@RequestBody @Valid JoinMemberDto memberDto) {
        Token token = service.join(memberDto.toMember());

        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

}
