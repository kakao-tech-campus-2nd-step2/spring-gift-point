package gift.member;

import gift.common.auth.AuthService;
import gift.member.model.MemberRequest;
import gift.member.model.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="Member API", description = "멤버 생성에 관한 API")
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Operation(summary = "회원 가입", description = "회원 가입을 한 후, Token을 반환합니다.")
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(
        @Valid @RequestBody MemberRequest memberRequest) {
        Long memberId = memberService.insertMember(memberRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberId))
            .body(authService.getToken(memberRequest));
    }

    @Operation(summary = "로그인", description = "로그인 한 후, Token을 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(
        @Valid @RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(authService.getToken(memberRequest));
    }
}
