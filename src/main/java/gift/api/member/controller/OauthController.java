package gift.api.member.controller;

import gift.api.member.dto.TokenResponse;
import gift.api.member.service.MemberFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2")
@Tag(name = "OAuth", description = "OAuth API")
public class OauthController {

    private final MemberFacade memberFacade;

    public OauthController(MemberFacade memberFacade) {
        this.memberFacade = memberFacade;
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "이메일 제공 동의 필요")
    })
    public ResponseEntity<TokenResponse> loginKakao(
        @Parameter(required = true, description = "카카오 인가 코드")
        @RequestParam("code") String code) {

        return ResponseEntity.ok().body(memberFacade.loginKakao(code));
    }
}
