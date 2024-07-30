package gift.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증 토큰 API")
public interface KakaoApi {

    @Operation(summary = "카카오 로그인을 통해 회원을 인증하고 토큰을 등록한다.")
    ResponseEntity<Void> redirectSetToken(Long memberId);
}
