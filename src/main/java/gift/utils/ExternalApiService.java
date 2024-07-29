package gift.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoOrderResponse;
import gift.controller.dto.KakaoTokenDto;
import gift.domain.Order;
import gift.domain.Token;
import gift.repository.TokenRepository;
import gift.utils.config.KakaoProperties;
import gift.utils.error.UserNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;
    private final String baseauthUrl = "https://kauth.kakao.com/oauth";
    private final String baseloginUrl = "https://kapi.kakao.com";
    private final ObjectMapper objectMapper;
    private final TokenRepository tokenRepository;

    public ExternalApiService(RestTemplate restTemplate, KakaoProperties kakaoProperties,
        ObjectMapper objectMapper, TokenRepository tokenRepository) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;
        this.tokenRepository = tokenRepository;
    }


    public ResponseEntity<KakaoTokenDto> postKakaoToken(String code) {
        String endpoint = "/token";
        UriComponentsBuilder fullUrl = UriComponentsBuilder.fromHttpUrl(baseauthUrl + endpoint);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoProperties.getRestApiKey());
        requestBody.add("redirect_uri", kakaoProperties.getRedirectUri());
        requestBody.add("code", code);
//            client_secret이
//            if (kakaoProperties.getClientSecret() != null && !kakaoProperties.getClientSecret().isEmpty()) {
//                params.add("client_secret", kakaoProperties.getClientSecret());
//            }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody,
            headers);

        try {
            ResponseEntity<KakaoTokenDto> response = restTemplate.exchange(
                fullUrl.toUriString(),
                HttpMethod.POST,
                requestEntity,
                KakaoTokenDto.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            // 400번대 에러
            logger.error("클라이언트 에러: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (HttpServerErrorException e) {
            // 500번대 에러
            logger.error("카카오 API 서버 에러: {}", e.getMessage());
            return ResponseEntity.status(e.getStatusCode()).body(null);
        } catch (ResourceAccessException e) {
            // 네트워크 에러
            logger.error("카카오 API 네트워크 에러: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (RestClientException e) {
            // RestTemplate 에러
            logger.error("RestTemlpate 에러: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public String postKakaoUserId(String accessToken) {
        String endpoint = "/v2/user/me";
        UriComponentsBuilder fullUrl = UriComponentsBuilder.fromHttpUrl(baseloginUrl + endpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
            );

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (jsonNode.has("id")) {
                return jsonNode.get("id").asText();
            } else {
                throw new RuntimeException("카카오 id 찾기 실패");
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("클라이언트 요청 에러: " + e.getStatusCode(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("카카오 api 요청 서버 에러: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("네트워크 에러", e);
        } catch (RestClientException e) {
            throw new RuntimeException("카카오 api 요청 에러", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("api response 파싱 실패", e);
        }

    }

    public String postSendMe(KakaoApiDTO.KakaoOrderResponse kakaoOrderResponse,String accessToken){
        String endpoint = "/v2/api/talk/memo/default/send";

        UriComponentsBuilder fullUrl = UriComponentsBuilder.fromHttpUrl(baseloginUrl + endpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String orderInfoText = makeMessageBody(kakaoOrderResponse);


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_object", orderInfoText);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(fullUrl.toUriString(), requestEntity, String.class);



        // 응답 처리
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to send Kakao message: " + response.getStatusCode());
        }



    }
    public String refreshToken(String email,String refreshToken){
        String endpoint = "/token";
        UriComponentsBuilder fullUrl = UriComponentsBuilder.fromHttpUrl(baseauthUrl + endpoint);
        KakaoProperties kakaoProperties1 = new KakaoProperties();
        String restApiKey = kakaoProperties1.getRestApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", restApiKey);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            fullUrl.toUriString(),
            HttpMethod.POST,
            requestEntity,
            String.class
        );
        Token token = tokenRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User not found")
        );

        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (jsonNode.has("access_token")) {
                String accessToken = jsonNode.get("access_token").asText();
                token.updateAccesstoken(accessToken);
            } else {
                throw new RuntimeException("카카오 id 찾기 실패");
            }
        }catch (HttpClientErrorException e) {
            throw new RuntimeException("클라이언트 요청 에러: " + e.getStatusCode(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("카카오 api 요청 서버 에러: " + e.getStatusCode(), e);
        } catch (ResourceAccessException e) {
            throw new RuntimeException("네트워크 에러", e);
        } catch (RestClientException e) {
            throw new RuntimeException("카카오 api 요청 에러", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("api response 파싱 실패", e);
        }


        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // 새로운 액세스 토큰 정보가 포함된 JSON 문자열 반환
        } else {
            throw new RuntimeException("Failed to refresh token. Status code: " + response.getStatusCode());
        }

    }



    private String makeMessageBody(KakaoApiDTO.KakaoOrderResponse kakaoOrderResponse ) {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"object_type\":\"text\",\"text\":\"")
            .append(kakaoOrderResponse.id())
            .append(" 번 옵션이 주문 되었습니다. ")
            .append(kakaoOrderResponse.message())
            .append("\",\"link\":{\"web_url\":\"\"}}");

        return  sb.toString();
    }



}
