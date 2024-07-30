package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gift.authorization.JwtUtil;
import gift.entity.Member;
import gift.entity.MemberType;
import gift.exception.KakaoLoginException;
import gift.repository.MemberRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Value("${kakao.login.REST-API-KEY}")
    private String loginRestApiKey;

    @Value("${kakao.login.REDIRECT-URI}")
    private String loginRedirectUri;

    RestTemplate restTemplate = new RestTemplate();

    public KakaoLoginService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String makeKakaoAuthorizationURI() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=")
                .append(loginRestApiKey)
                .append("&redirect_uri=")
                .append(loginRedirectUri)
                .append("&scope=account_email");

        return stringBuilder.toString();
    }


    //RequestEntity<LinkedMultiValueMap<String, String>>
    public String getKakaoAuthorizationToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", loginRestApiKey);
        body.add("redirect_uri", loginRedirectUri);
        body.add("code", code);
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<String> response = restTemplate.exchange(
                request, String.class);

        String jsonResponse = response.getBody();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        String accessToken = jsonObject.getString("access_token");

        return accessToken;
    }

    public void validationKaKaoToken(String accessToken){
        String url = "https://kapi.kakao.com/v1/user/access_token_info";
        var headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        }catch(Exception e) {
            throw new KakaoLoginException("카카오 로그인 오류 발생");
        }


    }//HttpClientErrorException

    public String getKakaoEmailByToken(String accessToken){

        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String requestBody = "";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String email = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode kakaoAccountNode = rootNode.path("kakao_account");
            email = kakaoAccountNode.path("email").asText();
            // 이메일 출력
            System.out.println("Email: " + email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
    
    // email 있으면 -> 토큰 생성 (우리 서버), 만약 없으면 회원 가입 후 토큰 발급
    public String checkEmail(String email){
        Member member;
        if(!memberRepository.existsByEmail(email)){
            member = new Member(email, MemberType.KAKAO);
            memberRepository.save(member);
        }else{
            member = memberRepository.findByEmail(email)
                    .get();
        }

        return jwtUtil.generateToken(member);
    }

   


}
