package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.OAuthToken;
import gift.dto.KakaoResponse;
import gift.dto.OAuthTokenDto;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping
public class OauthController {

    @Value("${kakao.rest-api-key}")
    private String restApiKey;

    private final MemberService memberService;
    private final RestTemplate restTemplate;

    public OauthController(MemberService memberService, RestTemplate restTemplate) {
        this.memberService = memberService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/oauth/kakao")
    public void kakaologin(HttpServletResponse response) throws IOException {
        String kakaoUrl= "https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code&redirect_uri=http://localhost:8080&client_id=" + restApiKey;
        response.sendRedirect(kakaoUrl);
    }

    @GetMapping
    public ResponseEntity<String> kakaoCallback(@RequestParam String code) {

        // header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", restApiKey);
        params.add("redirect_uri", "http://localhost:8080");
        params.add("code", code);
        // header + body
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // POST 방식으로 Http 요청, 응답 저장
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            OAuthToken oAuthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
            return ResponseEntity.status(HttpStatus.OK).body(oAuthToken.getAccess_token());
        }catch (JsonMappingException e){
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // OAuth register
    @PostMapping("/oauth/register")
    public ResponseEntity<String> register(@RequestBody OAuthTokenDto oAuthTokenDto){

        String accessToken = oAuthTokenDto.getAccessToken();
        String password = oAuthTokenDto.getPassword();

        // Header
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer "+accessToken);
        header.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
        // Body
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        params.add("property_keys","[\"kakao_account.email\"]");

        // header랑 body 합치기
        HttpEntity<MultiValueMap<String,Object>> kakaoProfileRequest = new HttpEntity<>(params,header);

        // post로 http 요청을 보내고 응답을 받는다.
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoProfileRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            KakaoResponse kakaoResponse = objectMapper.readValue(response.getBody(),KakaoResponse.class);
            String email = kakaoResponse.getKakao_account().getEmail();
            memberService.oauthSave(email,password,accessToken);
        }catch (JsonMappingException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON format");
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON processing error");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("OAuth를 사용하여 유저 생성 완료");
    }
}
