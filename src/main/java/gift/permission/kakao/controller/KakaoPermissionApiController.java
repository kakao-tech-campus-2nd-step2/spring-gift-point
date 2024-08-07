package gift.permission.kakao.controller;

import static org.springframework.http.ResponseEntity.ok;

import gift.global.dto.TokenDto;
import gift.permission.kakao.service.KakaoPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// kakao 로그인 핸들러를 갖고 있는 컨트롤러
@RestController
public class KakaoPermissionApiController {

    private final KakaoPermissionService kakaoPermissionService;

    @Autowired
    public KakaoPermissionApiController(KakaoPermissionService kakaoPermissionService) {
        this.kakaoPermissionService = kakaoPermissionService;
    }

    // 302 redirect url
    @GetMapping("/api/login/kakao/authorization")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestParam(name = "code") String code) {
        return ok(kakaoPermissionService.kakaoLogin(code));
    }
}
