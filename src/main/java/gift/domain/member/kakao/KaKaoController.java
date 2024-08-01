package gift.domain.member.kakao;

import gift.domain.member.Member;
import gift.domain.member.dto.response.MemberResponse;
import gift.global.jwt.JwtProvider;
import gift.global.response.ResponseMaker;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
@Tag(name = "KaKao", description = "KaKao API")
public class KaKaoController {

    private final KaKaoService kaKaoService;

    public KaKaoController(KaKaoService kaKaoService) {
        this.kaKaoService = kaKaoService;
    }

    /**
     * 카카오 로그인 페이지로 이동
     */
    @GetMapping("/api/oauth/kakao")
    @Operation(summary = "카카오 로그인 페이지로 이동")
    public RedirectView LoginPage() {
        return new RedirectView(kaKaoService.buildLoginPageUrl());
    }

    /**
     * 카카오 로그인 인가코드로 JWT 발급
     */
    @GetMapping
    @Operation(summary = "카카오 로그인 인가코드로 JWT 발급")
    public ResponseEntity<MemberResponse> getAuth(
        @Parameter(description = "카카오 로그인 인가코드") @RequestParam(value = "code") String authorizedCode
    ) {
        KaKaoToken kaKaoToken = kaKaoService.getKaKaoToken(authorizedCode);
        Member findMember = kaKaoService.loginOrRegister(kaKaoToken);

        String jwt = JwtProvider.generateToken(findMember);
        MemberResponse memberResponse = new MemberResponse(findMember.getEmail(), jwt);
        return ResponseEntity.ok(memberResponse);
    }
}
