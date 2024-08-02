package gift.docs.oauth;

import gift.global.authentication.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "OAuth", description = "OAuth 관련 API")
public interface OAuthApiDocs {

    @Hidden
    public ResponseEntity<AuthResponse> getKakaoCode(@RequestParam String code);

    @Operation(summary = "카카오 로그인")
    public ResponseEntity<Void> singInKaKao();
}
