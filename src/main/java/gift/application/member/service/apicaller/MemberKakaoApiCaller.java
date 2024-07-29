package gift.application.member.service.apicaller;

import static io.jsonwebtoken.Header.CONTENT_TYPE;

import gift.application.member.dto.MemberKakaoModel;
import gift.global.config.KakaoProperties;
import gift.global.validate.TimeOutException;
import java.net.URI;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class MemberKakaoApiCaller {

    private final KakaoProperties kakaoProperties;
    private final RestTemplate restTemplate;

    public MemberKakaoApiCaller(KakaoProperties kakaoProperties, RestTemplate restTemplate) {
        this.kakaoProperties = kakaoProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * 토큰을 사용해서 사용자 정보를 가져옴
     */
    public MemberKakaoModel.MemberInfo getMemberInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var request = new RequestEntity<>(headers, HttpMethod.GET,
            URI.create(kakaoProperties.userRequestUri()));

        try {
            return restTemplate.exchange(request,
                MemberKakaoModel.MemberInfo.class).getBody();

        } catch (ResourceAccessException e) {
            throw new TimeOutException("네트워크 연결이 불안정 합니다.", e);
        }
    }
}
