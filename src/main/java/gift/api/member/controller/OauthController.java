package gift.api.member.controller;

import gift.api.member.service.MemberFacade;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth2")
public class OauthController {

    private final MemberFacade memberFacade;

    public OauthController(MemberFacade memberFacade) {
        this.memberFacade = memberFacade;
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 로그인")
    public ResponseEntity<Void> loginKakao(@RequestParam("code") String code) {
        memberFacade.loginKakao(code);
        return ResponseEntity.ok().build();
    }
}
