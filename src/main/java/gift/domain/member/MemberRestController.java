package gift.domain.member;

import gift.domain.member.dto.request.MemberRequest;
import gift.domain.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member", description = "Member API")
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    @Operation(summary = "회원가입")
    public ResponseEntity<MemberResponse> join(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.join(memberRequest);

        return ResponseEntity.ok(memberResponse);
    }

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인을 한 후 JWT 토큰을 발급받는다.")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.login(memberRequest);

        return ResponseEntity.ok(memberResponse);
    }

}
