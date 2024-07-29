package gift.domain.Member;

import gift.domain.Member.dto.MemberDTO;
import gift.global.response.ResponseMaker;
import gift.global.response.SimpleResultResponseDto;
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
    public ResponseEntity<SimpleResultResponseDto> join(@Valid @RequestBody MemberDTO memberDTO) {
        memberService.join(memberDTO);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "회원 가입에 성공했습니다");
    }

    /**
     * 회원 로그인
     */
    @PostMapping("/login")
    @Operation(summary = "로그인을 한 후 JWT 토큰을 발급받는다.")
    public ResponseEntity<SimpleResultResponseDto> login(@Valid @RequestBody MemberDTO memberDTO) {
        String jwt = memberService.login(memberDTO);

        return ResponseMaker.createSimpleResponseWithJwtOnHeader(HttpStatus.OK, "로그인에 성공했습니다", jwt);
    }

}
