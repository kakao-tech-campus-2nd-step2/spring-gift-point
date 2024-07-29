package gift.member;

import gift.member.dto.MemberRequestDTO;
import gift.token.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member", description = "Member API")
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/register")
    @Operation(summary = "사용자 등록", description = "사용자를 등록하고, 액세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "등록 성공")
    @ApiResponse(responseCode = "400", description = "사용자가 이미 존재하거나, 입력 양식이 잘못되었습니다.")
    public ResponseEntity<Token> register(@RequestBody MemberRequestDTO memberRequestDTO) {
        return ResponseEntity.ok(new Token(memberService.register(memberRequestDTO)));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 정보를 확인하고, 액세스 토큰을 발급합니다.")
    @ApiResponse(responseCode = "200", description = "인증 완료")
    @ApiResponse(responseCode = "400", description = "존재하지 않는 사용자거나, 잘못된 입력 양식입니다.")
    public ResponseEntity<Token> login(@RequestBody MemberRequestDTO memberRequestDTO) {
        return ResponseEntity.ok(new Token(memberService.login(memberRequestDTO)));
    }
}
