package gift.service;

import java.net.URI;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import gift.entity.User;
import gift.exception.UnauthorizedException;
import gift.repository.UserRepository;

@Service
public class KakaoAuthService implements TokenHandler{

    @Value("${kakao.client-id}")
    public String clientId;

    @Value("${kakao.redirect-uri}")
    public String redirectUri;

    @Value("${kakao.auth-url}")
    public String authUrl;
    
    @Value("${kakao.token-info-url}")
    public String tokenInfoUrl;
    
    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public KakaoAuthService(RestTemplate restTemplate, UserRepository userRepository,
    		TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        tokenService.addTokenParser(this);
    }

    public Map<String, String> getAccessToken(String authorizationCode) {
        RequestEntity<MultiValueMap<String, String>> request = authRequest(authorizationCode);
        ResponseEntity<Map<String, String>> response = errorHandling(request,
        		new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> token = response.getBody();
        
        String accessToken = token.get("access_token");
        registerUser(accessToken);
        
        return token;
    }

    private RequestEntity<MultiValueMap<String, String>> authRequest(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = createBody(authorizationCode);
        return new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(authUrl));
    }

    private MultiValueMap<String, String> createBody(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizationCode);
        body.add("scope", "account_email");
        return body;
    }

    public <T> ResponseEntity<T> errorHandling(RequestEntity<?> request, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(request, responseType);
    }
    
    @Override
    public String parseToken(String token) {
        RequestEntity<Void> request = tokenInfoRequest(token);
        ResponseEntity<Map<String, Object>> response = errorHandling(request,
        		new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("kakao_account")) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        return (String) kakaoAccount.get("email");
    }
    
    @Override
    public String getTokenSuffix() {
    	return "-kakao";
    }

    private void registerUser(String accessToken) {
    	String email = parseToken("Bearer " + accessToken);
    	String password = grantRandomPassword();
    	User newUser = new User(email, password);
        userRepository.save(newUser);
    }
    
    private String grantRandomPassword() {
    	return RandomStringUtils.random(15);
    }
    
    private RequestEntity<Void> tokenInfoRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        return new RequestEntity<>(headers, HttpMethod.GET, URI.create(tokenInfoUrl));
    }
}
