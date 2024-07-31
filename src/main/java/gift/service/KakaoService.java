package gift.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoService {

  @Value("${kakao.auth.url}")
  private String authUrl;

  @Value("${kakao.token.url}")
  private String tokenUrl;

  @Value("${kakao.client.id}")
  private String clientId;

  @Value("${kakao.client.secret}")
  private String clientSecret;

  @Value("${kakao.redirect.uri}")
  private String redirectUri;

  @Value("${kakao.api.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public KakaoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getAuthorizationUrl() {
    return UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
            .queryParam("response_type", "code")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .build()
            .toUriString();
  }

  public String getToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", clientId);
    params.add("redirect_uri", redirectUri);
    params.add("code", code);
    params.add("client_secret", clientSecret);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid or expired authorization code");
    }
  }

  public void sendMessage(String message, String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setBearerAuth(accessToken);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("template_object", createTemplateObject(message));

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    try {
      restTemplate.postForEntity(apiUrl, request, String.class);
    } catch (HttpClientErrorException e) {
      handleException(e);
    }
  }

  private void handleException(HttpClientErrorException e) {
    if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized: Invalid or expired access token");
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to send message", e);
    }
  }

  private String createTemplateObject(String message) {
    return "{\"object_type\":\"text\",\"text\":\"" + message + "\",\"link\":{\"web_url\":\"http://example.com\"}}";
  }
}