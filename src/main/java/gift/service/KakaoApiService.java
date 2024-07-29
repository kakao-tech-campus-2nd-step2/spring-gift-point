package gift.service;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gift.dto.KakaoProperties;
import gift.dto.request.MessageRequest;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import gift.dto.response.MessageResponse;
import gift.dto.response.RefreshTokenResponse;
import gift.exception.CustomException;


@Service
public class KakaoApiService {

    private final RestClient client;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper;


    public KakaoApiService(KakaoProperties kakaoProperties, ObjectMapper objectMapper){
        this.kakaoProperties = kakaoProperties;
        this.objectMapper = objectMapper;

        // HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // factory.setConnectTimeout(5000);

        this.client = RestClient
                .builder()
                .build();
       
    }

    public KakaoTokenResponse getToken(String code){
        
        var url = "https://kauth.kakao.com/oauth/token";
        var body = createBody(code);

        try {

            ResponseEntity<String> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
                
            if(response.getStatusCode().is2xxSuccessful()){

                String jsonBody = response.getBody();
                return objectMapper.readValue(jsonBody, KakaoTokenResponse.class);

            } else if(response.getStatusCode().is4xxClientError()){
                throw new CustomException("Bad Request " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else if(response.getStatusCode().is5xxServerError()){
                throw new CustomException("System error " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else {
                throw new CustomException("Unexpected response error " + response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (JsonProcessingException e){
            throw new CustomException("Error parsing token response", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new CustomException("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public RefreshTokenResponse refreshToken(String refreshToken){

        var url = "https://kauth.kakao.com/oauth/token";

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.getApiKey());
        body.add("refresh_token", refreshToken);
        try {

            ResponseEntity<String> response = client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
                
            if(response.getStatusCode().is2xxSuccessful()){

                String jsonBody = response.getBody();
                return objectMapper.readValue(jsonBody, RefreshTokenResponse.class);

            } else if(response.getStatusCode().is4xxClientError()){
                throw new CustomException("Bad Request " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else if(response.getStatusCode().is5xxServerError()){
                throw new CustomException("System error " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else {
                throw new CustomException("Unexpected response error " + response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (JsonProcessingException e){
            throw new CustomException("Error parsing token response", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new CustomException("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public KakaoUserInfoResponse getUserInfo(String accessToken){

        var url = "https://kapi.kakao.com/v2/user/me";
        
        try {
            ResponseEntity<String> response = client.post()
                    .uri(URI.create(url))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .toEntity(String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                String jsonBody = response.getBody();
                return objectMapper.readValue(jsonBody, KakaoUserInfoResponse.class);
            } else if(response.getStatusCode().is4xxClientError()){
                throw new CustomException("Bad Request " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else if(response.getStatusCode().is5xxServerError()){
                throw new CustomException("System error " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else {
                throw new CustomException("Unexpected response error " + response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (JsonProcessingException e){
            throw new CustomException("Error parsing token response", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new CustomException("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public MessageResponse sendMessage(MessageRequest messageRequest){

        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";


        String templateObject = String.format(
                "{\"object_type\": \"text\", \"text\": \"%s\", \"link\": {\"web_url\": \"testUrl\", \"mobile_web_url\": \"testMobileUrl\"}, \"button_title\": \"선물 확인\"}",
                messageRequest.getProduct().getName() + "order Complete"
        );

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        try {
            ResponseEntity<String> response = client.post()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + messageRequest.getAccessToken())
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);

            if(response.getStatusCode().is2xxSuccessful()){
                String jsonBody = response.getBody();
                return objectMapper.readValue(jsonBody, MessageResponse.class);
            } else if(response.getStatusCode().is4xxClientError()){
                throw new CustomException("Bad Request " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else if(response.getStatusCode().is5xxServerError()){
                throw new CustomException("System error " + response.getBody(), HttpStatus.valueOf(response.getStatusCode().value()));
            } else {
                throw new CustomException("Unexpected response error " + response.getBody(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus());
        } catch (JsonProcessingException e){
            throw new CustomException("Error parsing token response", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new CustomException("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private LinkedMultiValueMap<String, String> createBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getApiKey());
        body.add("redirect_url", kakaoProperties.getRedirectUri());
        body.add("code", code);
        return body;

    }

    // private ClientHttpRequestFactory getClientHttpRequestFactory() {
    //     HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    //     clientHttpRequestFactory.setConnectTimeout(100);
    //     clientHttpRequestFactory.setConnectionRequestTimeout(70);
    //     return clientHttpRequestFactory;
    // }
}
