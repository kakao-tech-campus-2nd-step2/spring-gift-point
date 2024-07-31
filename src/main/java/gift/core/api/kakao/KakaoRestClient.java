package gift.core.api.kakao;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import gift.core.api.kakao.dto.KakaoUserInfo;

@HttpExchange()
public interface KakaoRestClient {

	@GetExchange("https://kauth.kakao.com/oauth/token")
	ResponseEntity<KakaoToken> getKakaoToken(
		@RequestParam("client_id") String clientId,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("grant_type") String grantType,
		@RequestParam("code") String code
	);


	@GetExchange("https://kapi.kakao.com/v2/user/me")
	ResponseEntity<KakaoUserInfo> getKakaoUserInfo(
		@RequestParam("access_token") String accessToken,
		@RequestBody List<String> propertyKeys
	);

	@PostExchange("https://kapi.kakao.com/v2/api/talk/memo/default/send")
	ResponseEntity<?> sendKakaoMessage(
		@RequestHeader("Content-Type") String contentType,
		@RequestHeader("Authorization") String accessToken,
		@RequestBody String template_object
	);
}
