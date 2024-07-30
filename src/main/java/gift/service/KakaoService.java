package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.KakaoAccessTokenDTO;
import gift.dto.KakaoUserInfoDTO;
import gift.dto.MemberDTO;
import gift.dto.OrderResponseDTO;
import gift.dto.TemplateObjectDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import java.net.URI;
import java.util.Random;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public KakaoService(KakaoProperties kakaoProperties, MemberRepository memberRepository,
        JwtUtil jwtUtil) {
        this.kakaoProperties = kakaoProperties;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public String generateKakaoLoginUrl() {
        return kakaoProperties.generateLoginUrl();
    }

    public String getAccessToken(String authorizationCode) {
        String url = kakaoProperties.tokenUrl();
        final LinkedMultiValueMap<String, String> body = createBody(authorizationCode);
        ResponseEntity<KakaoAccessTokenDTO> response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessTokenDTO.class);
        KakaoAccessTokenDTO kakaoAccessTokenDTO = response.getBody();
        return kakaoAccessTokenDTO.accessToken();
    }

    private LinkedMultiValueMap<String, String> createBody(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", authorizationCode);
        return body;
    }

    public String getUserEmail(String accessToken) {
        String url = kakaoProperties.userInfoUrl();
        ResponseEntity<KakaoUserInfoDTO> response = restClient.get()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .toEntity(KakaoUserInfoDTO.class);
        KakaoUserInfoDTO kakaoUserInfoDTO = response.getBody();
        return kakaoUserInfoDTO.kakaoAccountDTO().email();
    }

    @Transactional
    public Member saveKakaoUser(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            String name = email.split("@")[0];
            String password = generateRandomPassword();
            MemberDTO memberDTO = new MemberDTO(name, email, password);
            member = new Member(null, memberDTO.name(), memberDTO.email(), memberDTO.password(),
                "user");
            memberRepository.save(member);
        }
        return member;
    }

    public String generateToken(String email, String role) {
        String jwtToken = jwtUtil.generateToken(email, role);
        return jwtToken;
    }

    public void sendKakaoMessage(String accessToken, OrderResponseDTO orderResponseDTO) {
        String url = kakaoProperties.sendMessageUrl();
        String templateObjectJson = getTemplateObjectJson(orderResponseDTO);
        final LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);
        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(url))
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    private static String getTemplateObjectJson(OrderResponseDTO orderResponseDTO) {
        TemplateObjectDTO templateObjectDTO = new TemplateObjectDTO(
            orderResponseDTO.id(),
            orderResponseDTO.optionId(),
            orderResponseDTO.quantity(),
            orderResponseDTO.orderDateTime().toString(),
            orderResponseDTO.message()
        );
        ObjectMapper objectMapper = new ObjectMapper();
        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObjectDTO);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON으로 변환에 실패했습니다.");
        }
        return templateObjectJson;
    }

    private String generateRandomPassword() {
        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 20;
        Random random = new Random();
        String generatedPassword = random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        return generatedPassword;
    }
}