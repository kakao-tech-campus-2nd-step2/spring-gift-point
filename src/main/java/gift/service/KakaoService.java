package gift.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.model.User;
import gift.repository.UserRepository;
import gift.security.KakaoTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class KakaoService {

    private static final Logger LOGGER = Logger.getLogger(KakaoService.class.getName());

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final KakaoTokenProvider kakaoTokenProvider;

    @Autowired
    public KakaoService(KakaoTokenProvider kakaoTokenProvider, UserRepository userRepository) {
        this.kakaoTokenProvider = kakaoTokenProvider;
        this.userRepository = userRepository;
    }

    @Transactional
    public String login(String code) throws Exception {
        LOGGER.info("Logging in with code: " + code);
        String token = kakaoTokenProvider.getToken(code);
        LOGGER.info("Obtained token: " + token);
        User kakaoUser = getKakaoUserInfo(token);
        LOGGER.info("Kakao user info: " + kakaoUser.getEmail());

        User user = userRepository.findByEmail(kakaoUser.getEmail()).orElseGet(() -> userRepository.save(kakaoUser));
        user.setKakaoAccessToken(token);
        userRepository.save(user);
        LOGGER.info("Saved token for user: " + user.getEmail());

        return token;
    }

    public User getKakaoUserInfo(String accessToken) throws Exception {
        var url = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(headers), String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        String email = jsonNode.path("kakao_account").path("email").asText();
        String password = UUID.randomUUID().toString();
        return new User(email, password);
    }

    @Transactional
    public void sendMessageToMe(String email, String message) throws Exception {
        LOGGER.info("Sending message to user: " + email);
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null || user.getKakaoAccessToken() == null) {
            throw new RuntimeException("사용자에 대한 액세스 토큰이 없습니다.");
        }

        String accessToken = user.getKakaoAccessToken();

        String templateObject = "{ \"object_type\": \"text\", \"text\": \"" + message + "\", \"link\": { \"web_url\": \"http://localhost:8080\", \"mobile_web_url\": \"http://localhost:8080\" }}";

        try {
            kakaoTokenProvider.sendMessage(accessToken, templateObject);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("this access token does not exist")) {
                throw new RuntimeException("액세스 토큰이 유효하지 않습니다. 다시 로그인해 주세요.");
            } else {
                throw e;
            }
        }
    }
}