package gift.kakaoLogin;

import gift.exception.ErrorCode;
import gift.exception.KakaoException;
import gift.user.KakaoUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

@Service
public class KakaoLoginService {

    @Value("${kakao.client-id}")
    private String REST_API_KEY;
    @Value("${kakao.redirect-url}")
    private String REDIRECT_URI;
    private final RestClient kakaoRestClient;
    private final KakaoUserRepository kakaoUserRepository;
    private static final Logger log = LoggerFactory.getLogger(KakaoLoginService.class);

    public KakaoLoginService(RestClient kakaoRestClient, KakaoUserRepository kakaoUserRepository) {
        this.kakaoRestClient = kakaoRestClient;
        this.kakaoUserRepository = kakaoUserRepository;
    }

    public KakaoResponse login(String code){
        var url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = createBody(code);

        ResponseEntity<KakaoResponse> response = kakaoRestClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(status -> status.value() != 200, ((request, response1) -> {
                    log.info("[ERROR] " + response1.getStatusCode());
                    ErrorCode errorCode = getKauthErrorCode(response1);
                    throw new KakaoException(errorCode);
                }))
                .toEntity(KakaoResponse.class);

        return response.getBody();
    }


    public Long getUserInfo(String accessToken){
        var url = "https://kapi.kakao.com/v2/user/me";

        ResponseEntity<KakaoUserInfoResponse> response = kakaoRestClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class);

        return Objects.requireNonNull(response.getBody()).getId();
    }


    private static ErrorCode getKauthErrorCode(ClientHttpResponse response) throws IOException {
        if(!response.getHeaders().containsKey("WWW-Authenticate")){
            return new ErrorCode(HttpStatus.valueOf(response.getStatusCode().value()), response.getStatusCode().toString(), response.getStatusText());

        }
        String[] split = response.getHeaders().get("WWW-Authenticate").get(0).split(",");
        String error = split[1].split("=")[1].replaceAll("\"", "");
        String errorDescription = split[2].split("=")[1].replaceAll("\"", "");

        ErrorCode errorCode = new ErrorCode(HttpStatus.valueOf(response.getStatusCode().value()), error, errorDescription);
        return errorCode;
    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_url", REDIRECT_URI);
        body.add("code", code);
        return body;
    }

}
