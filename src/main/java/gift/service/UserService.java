package gift.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gift.entity.kakao.KakaoErrorCode;
import gift.entity.kakao.KakaoProperties;
import gift.entity.user.User;
import gift.entity.user.UserDTO;
import gift.exception.KakaoException;
import gift.exception.ResourceNotFoundException;
import gift.repository.UserRepository;
import gift.util.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private KakaoProperties kakaoProperties;

    private final RestClient client = RestClient.builder().build();

    private final UserRepository userRepository;
    private final UserUtility userUtility;

    @Autowired
    public UserService(UserRepository userRepository, UserUtility userUtility) {
        this.userRepository = userRepository;
        this.userUtility = userUtility;
    }

    public User findOne(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public String signup(UserDTO userDTO) {
        // 이미 존재하는 이메일
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // pw 암호화해서 저장
        String hashpw = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
        userDTO.setPassword(hashpw);

        User user = userRepository.save(new User(userDTO));
        return userUtility.makeAccessToken(user);
    }

    public String login(UserDTO userDTO) {
        // 존재하지 않는 이메일
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email does not exist"));

        // pw 틀린 경우
        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        return userUtility.makeAccessToken(user);
    }

    public String kakaoLogin(String code) {
        // access token 받아오기
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", code);

        String accessTokenResponse;
        try {
            accessTokenResponse = client.post()
                    .uri(URI.create("https://kauth.kakao.com/oauth/token"))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            String statusCode = e.getMessage().split(" ")[0];
            KakaoErrorCode errorCode = KakaoErrorCode.determineKakaoErrorCode(statusCode);
            throw new ResponseStatusException(errorCode.getStatus(), errorCode.getMessage());
        }

        JsonObject jsonObject = JsonParser.parseString(accessTokenResponse).getAsJsonObject();
        String kakaoAccessToken = jsonObject.get("access_token").getAsString();

        return kakaoAccessToken;
    }

    @Transactional
    public Map<String, String> getKakaoProfile(String kakaoAccessToken) {
        // 유저 정보 받아오기
        String userDataResponse;
        try {
            userDataResponse = client.post()
                    .uri(URI.create("https://kapi.kakao.com/v2/user/me"))
                    .header("Authorization", "Bearer " + kakaoAccessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException e) {
            String statusCode = e.getMessage().split(" ")[0];
            KakaoErrorCode errorCode = KakaoErrorCode.determineKakaoErrorCode(statusCode);
            throw new KakaoException(errorCode.getStatus(), errorCode.getMessage());
        }

        JsonObject jsonObject = JsonParser.parseString(userDataResponse).getAsJsonObject();
        JsonObject kakaoObject = jsonObject.get("kakao_account").getAsJsonObject();
        String email = kakaoObject.get("email").getAsString();

        if (!userRepository.existsByEmail(email)) {
            userRepository.save(new User(email, ""));
        }

        User user = userRepository.findByEmail(email).get();
        String accessToken = userUtility.makeAccessToken(user);

        Map<String, String> result = new HashMap<>();
        result.put("email", email);
        result.put("accessToken", accessToken);

        return result;
    }
}
