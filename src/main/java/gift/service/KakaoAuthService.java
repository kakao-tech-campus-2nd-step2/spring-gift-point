package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.OrderDTO;
import gift.model.*;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KakaoAuthService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private final RestClient client;

    private final KakaoProperties kakaoProperties;
    private final MemberRepository memberRepository;

    public KakaoAuthService(RestClient.Builder builder, KakaoProperties kakaoProperties, MemberRepository memberRepository) {
        this.kakaoProperties = kakaoProperties;
        this.memberRepository = memberRepository;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(kakaoProperties.getConnectTimeout());
        requestFactory.setReadTimeout(kakaoProperties.getReadTimeout());

        this.client = builder
                .requestFactory(requestFactory)
                .requestInterceptor(new ClientInterceptor())
                .build();
    }


    public String getKakaoToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";

        var body = createBody(code);
        var response =  this.client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoAuth.class);
        logger.info("getKakaoToekon" + response);

        //email 카카오 아이디로
        Long memberid = getKakakoMemberId(response.getBody().getAccessToken());
        if(!memberRepository.existsByEmail(memberid.toString())){
            Member member = new Member(memberid.toString(), "password");
            memberRepository.save(member);
        }

        return response.getBody().getAccessToken();
    }

    public Member getDBMemberByToken(String token){
        Long dbMemberId = getKakakoMemberId(token);
        return memberRepository.findByEmail(dbMemberId.toString())
                .orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));
    }

    public Long getKakakoMemberId(String token){
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var response = this.client.get()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                })
                .retrieve()
                .toEntity(KakaoMember.class);
        logger.info("getKakaoMeberId{}", response);
        return response.getBody().getId();
    }

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);
        return body;
    }
}
