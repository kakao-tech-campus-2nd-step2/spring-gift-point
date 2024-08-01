package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthService {

  @Value("${kakao.client.id}")
  private String clientId;

  @Value("${kakao.client.secret}")
  private String clientSecret;

  @Value("${kakao.redirect.uri}")
  private String redirectUri;

  @Value("${kakao.token.url}")
  private String tokenUrl;

  @Value("${kakao.auth.url}")
  private String authUrl;

  private final RestTemplate restTemplate;

  public AuthService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  public String getAuthorizationUrl() {
    return UriComponentsBuilder.fromHttpUrl(authUrl)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .toUriString();
  }

  public String getToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/x-www-form-urlencoded");

    String body = "grant_type=authorization_code&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUri + "&code=" + code;
    HttpEntity<String> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

    if (response.getStatusCode() != HttpStatus.OK) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to retrieve token");
    }

    return parseToken(response.getBody());
  }

  private String parseToken(String responseBody) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(responseBody);
      return root.path("access_token").asText();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to parse token");
    }
  }
}