package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.KakaoToken;
import gift.dto.KakaoTalkRequest;
import gift.dto.KakaoTalkResponse;
import gift.dto.MemberDTO;
import gift.exception.InvalidKakaoTalkTemplateException;
import gift.exception.InvalidKakaoTokenException;
import gift.exception.NoKakaoTokenException;
import gift.repository.KakaoTokenRepository;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoTalkService {

    private final RestTemplate restTemplate;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String KAKAO_SEND_TALK_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    @Autowired
    public KakaoTalkService(RestTemplateBuilder restTemplateBuilder, KakaoTokenRepository kakaoTokenRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public KakaoTalkResponse sendTalk(MemberDTO memberDTO, KakaoTalkRequest kakaoTalkRequest) {
        String templateObject;
        try {
            templateObject = objectMapper.writeValueAsString(kakaoTalkRequest);
        } catch (JsonProcessingException e) {
            throw new InvalidKakaoTalkTemplateException();
        }
        KakaoToken kakaoToken = kakaoTokenRepository.findById(memberDTO.email())
            .orElseThrow(NoKakaoTokenException::new);

        var client = RestClient.builder(restTemplate).build();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObject);
        try {
            return client.post()
                .uri(URI.create(KAKAO_SEND_TALK_URL))
                .header("Authorization", "Bearer " + kakaoToken.getAccessToken())
                .body(body)
                .retrieve()
                .body(KakaoTalkResponse.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidKakaoTokenException(e.getStatusCode(), e.getMessage());
        }
    }
}
