package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

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

  @Value("${kakao.user.info.url}")
  private String userInfoUrl;

  private final RestTemplate restTemplate;

  public AuthService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getAuthorizationUrl() {
    String url = UriComponentsBuilder.fromHttpUrl(authUrl)
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .toUriString();
    logger.info("Authorization URL: {}", url);
    return url;
  }

  public String getToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    String body = "grant_type=authorization_code&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + redirectUri + "&code=" + code;
    HttpEntity<String> entity = new HttpEntity<>(body, headers);

    logger.info("Requesting token with body: {}", body);
    try {
      ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
      if (response.getStatusCode() != HttpStatus.OK) {
        logger.error("Failed to retrieve token. Status code: {}", response.getStatusCode());
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to retrieve token");
      }
      logger.info("Token response: {}", response.getBody());
      return parseToken(response.getBody());
    } catch (Exception e) {
      logger.error("Error retrieving token", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve token");
    }
  }

  private String parseToken(String responseBody) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(responseBody);
      String accessToken = root.path("access_token").asText();
      logger.info("Parsed access token: {}", accessToken);
      return accessToken;
    } catch (Exception e) {
      logger.error("Error parsing token", e);
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to parse token");
    }
  }

  public JsonNode getKakaoUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<String> entity = new HttpEntity<>(headers);

    logger.info("Requesting user info with access token: {}", accessToken);
    try {
      ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
      if (response.getStatusCode() != HttpStatus.OK) {
        logger.error("Failed to retrieve user info. Status code: {}", response.getStatusCode());
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Failed to retrieve user info");
      }
      logger.info("User info response: {}", response.getBody());
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readTree(response.getBody());
    } catch (Exception e) {
      logger.error("Error retrieving user info", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve user info");
    }
  }
}