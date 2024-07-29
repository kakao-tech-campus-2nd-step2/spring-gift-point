package gift.kakao.login.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.kakao.login.dto.KakaoMessageSendResponse;
import gift.kakao.login.dto.KakaoTokenResponseDTO;
import gift.kakao.login.dto.KakaoUser;
import gift.kakao.login.dto.KakaoUserInfoResponse;
import gift.kakao.login.dto.LinkObject;
import gift.kakao.login.dto.TemplateObject;
import gift.user.repository.UserRepository;
import gift.utility.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {
    private final RestClient client;

    private final String clientId;
    private final String redirectUri;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    public KakaoLoginService(@Value("${kakao.client-id}") String clientId,
        @Value("${kakao.redirect-uri}") String redirectUri,
        UserRepository userRepository,
        RestClient client,
        ObjectMapper objectMapper){
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.userRepository = userRepository;
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public String getAccessToken(String code){
        var uri = "https://kauth.kakao.com/oauth/token";
        final var body = createAccessTokenBody(code);
        var response =  client.post()
            .uri(URI.create(uri))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoTokenResponseDTO.class);
        String accessToken = response.getBody().getAccessToken();
        return JwtUtil.generateToken(accessToken);
    }
    public String getUserInfo(String jwtAccessToken){
        var uri = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setBearerAuth(JwtUtil.extractEmail(jwtAccessToken));
        headers.add("property_keys", "[\"kakao_account.email\"]");

        var response = client.get()
            .uri(URI.create(uri))
            .headers(httpHeaders -> httpHeaders.addAll(headers))
            .retrieve()
            .toEntity(KakaoUserInfoResponse.class);
        String email = response.getBody().getId();
        String password = "";
        if(userRepository.findByEmail(email).isEmpty()){
            userRepository.save(new KakaoUser(email, password, jwtAccessToken));
            return email;
        }
        KakaoUser kakaoUser = (KakaoUser) userRepository.findByEmail(email).get();
        kakaoUser.setToken(jwtAccessToken);
        userRepository.save(kakaoUser);
        return email;
    }

    public KakaoMessageSendResponse sendMessage(String jwtAccessToken, String message){
        String accessToken = JwtUtil.extractEmail(jwtAccessToken);
        var uri = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var body = createSendMsgBody(message);
        var response = client.post()
            .uri(uri)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoMessageSendResponse.class);
        return response.getBody();
    }

    private @NotNull LinkedMultiValueMap<String, String> createAccessTokenBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        return body;
    }

    private @NotNull MultiValueMap<String, String> createSendMsgBody(String message){
        LinkObject link = new LinkObject("www.naver.com");
        TemplateObject template = new TemplateObject("text", message, link);

        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(template);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonString);

        return body;
    }

}
