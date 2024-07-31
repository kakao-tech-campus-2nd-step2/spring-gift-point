package gift.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.api.kakao.KakaoRestClient;
import gift.core.api.kakao.dto.KakaoUserInfo;

@Service
public class OAuthservice {

	private final KakaoRestClient kakaoRestClient;
	private final CacheManager cacheManager;

	@Value("${kakao.clientId}")
	private String clientId;
	@Value("${kakao.redirectUri}")
	private String redirectUri;
	@Value("${kakao.grantType}")
	private String grantType;

	public OAuthservice(KakaoRestClient kakaoRestClient, CacheManager cacheManager) {
		this.kakaoRestClient = kakaoRestClient;
		this.cacheManager = cacheManager;
	}

	// kakao api와 통신해서 accessToken과 유저 정보를 받아온 후, cache에 저장.
	@Transactional
	public String authenticate(String code) {
		String accessToken = kakaoRestClient.getKakaoToken(clientId, redirectUri, grantType, code)
			.getBody().access_token();

		KakaoUserInfo kakaoUserInfo = kakaoRestClient.getKakaoUserInfo(accessToken, List.of("kakao_account"))
			.getBody();

		cacheManager.getCache("userToken").put(kakaoUserInfo.kakao_account().email(), accessToken);

		return kakaoUserInfo.kakao_account().email();
	}
}
