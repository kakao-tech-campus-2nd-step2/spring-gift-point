package gift.api.member.service;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import gift.api.member.dto.TokenResponse;
import gift.api.member.dto.UserInfoResponse;
import gift.api.member.repository.MemberRepository;
import gift.api.order.dto.MsgMeResponse;
import gift.api.order.exception.MessageFailException;
import gift.global.config.KakaoProperties;
import gift.global.exception.NoSuchEntityException;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final MemberRepository memberRepository;
    private final KakaoProperties properties;
    private final RestClient restClient;

    public KakaoService(MemberRepository memberRepository, KakaoProperties properties) {
        this.memberRepository = memberRepository;
        this.properties = properties;
        restClient = RestClient.create();
    }

    public ResponseEntity<TokenResponse> obtainToken(String code) {
        return restClient.post()
            .uri(URI.create(properties.url().token()))
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(createBody(code))
            .retrieve()
            .toEntity(TokenResponse.class);
    }

    public ResponseEntity<UserInfoResponse> obtainUserInfo(
        ResponseEntity<TokenResponse> tokenResponse) {
        return restClient.get()
            .uri(properties.url().user(), uriBuilder -> uriBuilder
                .queryParam("property_keys", "[\"kakao_account.email\"]")
                .build())
            .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
            .header(AUTHORIZATION, "Bearer " + tokenResponse.getBody().accessToken())
            .retrieve()
            .toEntity(UserInfoResponse.class);
    }

    public void sendMessage(Long memberId, LinkedMultiValueMap<Object, Object> body) {
        MsgMeResponse msgMeResponse = restClient.post()
            .uri(properties.url().defaultTemplateMsgMe())
            .header(HttpHeaders.AUTHORIZATION,
                "Bearer " + memberRepository.findById(memberId)
                    .orElseThrow(() -> new NoSuchEntityException("member"))
                    .getKakaoAccessToken())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(body)
            .retrieve()
            .body(MsgMeResponse.class);

        if (msgMeResponse.resultCode() != 0) {
            throw new MessageFailException();
        }
    }

    private LinkedMultiValueMap<Object, Object> createBody(String code) {
        var body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("redirect_url", properties.url().redirect());
        body.add("code", code);
        return body;
    }
}
