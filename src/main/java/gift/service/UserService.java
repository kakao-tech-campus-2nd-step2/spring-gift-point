package gift.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gift.Util.JWTUtil;

import gift.dto.user.*;

import gift.entity.KakaoUser;
import gift.entity.User;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.ServerInternalException;
import gift.exception.exception.UnAuthException;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;

    @Value("${kakao.client_id}")
    String client_id;
    @Value("${kakao.redirect_uri}")
    String redirect_uri;
    @Value("${kakao.token_url}")
    String token_url;
    @Value("${kakao.info_url}")
    String info_url;

    //https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id=
    String code = "";
    ObjectMapper objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();

    public UserResponseDTO signUp(SignUpDTO signUpDTO) {
        User newUser = new User(signUpDTO);
        userRepository.findByEmail(newUser.getEmail()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 계정");
        });
        newUser = userRepository.save(newUser);
        return new UserResponseDTO(newUser.getEmail(), JWTUtil.generateToken(newUser.getId(), null));
    }

    public UserResponseDTO signIn(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.email()).orElseThrow(() -> new NotFoundException("존재하지 않는 계정"));
        if (!user.getPassword().equals(loginDTO.password())) throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        return new UserResponseDTO(user.getEmail(), JWTUtil.generateToken(user.getId(), null));
    }

    private String getKakaoToken() {
        var url = token_url;
        var headers = makeTokenHeader();
        var body = makeTokenBody();

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity<String> response;
        KakaoToken kakaoToken;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
            kakaoToken = objectMapper.readValue(response.getBody(), KakaoToken.class);
        } catch (JsonProcessingException e) {
            throw new ServerInternalException("파싱 에러");
        }
        return kakaoToken.access_token();
    }

    private HttpHeaders makeTokenHeader() {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private LinkedMultiValueMap<String, String> makeTokenBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", client_id);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);
        return body;
    }

    private Long getKakaoUserId(String token) {
        var url = info_url;
        var headers = makeIdHeader(token);
        var body = makeIdBody();
        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JsonObject jsonObject = JsonParser.parseString(Objects.requireNonNull(response.getBody())).getAsJsonObject();
        return jsonObject.get("id").getAsLong();
    }

    private LinkedMultiValueMap<String, String> makeIdBody() {
        var body = new LinkedMultiValueMap<String, String>();
        return body;
    }

    private HttpHeaders makeIdHeader(String token) {
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return headers;
    }

    public UserResponseDTO kakaoLogin() {
        String kakaoToken = getKakaoToken();
        Long kakaoUserId = getKakaoUserId(kakaoToken);
        User user = findUserByKakaoUserId(kakaoUserId);
        String token = JWTUtil.generateToken(user.getId(), kakaoToken);
        return new UserResponseDTO(user.getEmail(), token);
    }

    private User findUserByKakaoUserId(Long kakaoUserId) {
        Optional<User> optionalUser = kakaoUserRepository.findByKakaoUserId(kakaoUserId);
        if (optionalUser.isPresent()) return optionalUser.get();

        User user = new User("Kakao ID" + kakaoUserId.toString(), "password" + kakaoUserId.toString());
        user = userRepository.save(user);
        KakaoUser kakaoUser = new KakaoUser(kakaoUserId, user);
        kakaoUserRepository.save(kakaoUser);
        return user;
    }


}
