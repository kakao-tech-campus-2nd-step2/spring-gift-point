package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.client.KakaoApiClient;
import gift.client.requestBody.KakaoMessageRequestBodyGenerator;
import gift.client.requestBody.KakaoTokenRequestBodyGenerator;
import gift.dto.request.OrderRequest;
import gift.dto.response.KakaoTokenResponse;
import gift.repository.KakaoAccessTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAccessTokenRepository kakaoAccessTokenRepository;
    private final ObjectMapper objectMapper;

    public KakaoApiService(KakaoApiClient kakaoApiClient, KakaoAccessTokenRepository kakaoAccessTokenRepository, ObjectMapper objectMapper) {
        this.kakaoApiClient = kakaoApiClient;
        this.kakaoAccessTokenRepository = kakaoAccessTokenRepository;
        this.objectMapper = objectMapper;
    }

    @Value("${clientId}")
    private String clientId;

    public KakaoTokenResponse getKakaoToken(String code) {
        KakaoTokenRequestBodyGenerator generator = new KakaoTokenRequestBodyGenerator(clientId, code);

        return kakaoApiClient.getKakaoToken(generator.toMultiValueMap());
    }

    public String getMemberEmail(String token) {
        return kakaoApiClient.getMemberEmail(token);
    }

    public void sendMessageToMe(Long memberId, OrderRequest orderRequest) {
        String accessToken = kakaoAccessTokenRepository.getAccessToken(memberId);

        KakaoMessageRequestBodyGenerator generator = new KakaoMessageRequestBodyGenerator(orderRequest.message(), objectMapper);

        kakaoApiClient.sendMessageToMe(accessToken, generator.toMultiValueMap());
    }
}
