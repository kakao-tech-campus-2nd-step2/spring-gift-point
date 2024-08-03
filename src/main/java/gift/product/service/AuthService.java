package gift.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.DuplicateRequestException;
import gift.product.dto.auth.AccessTokenDto;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.auth.MemberDto;
import gift.product.dto.auth.OAuthJwt;
import gift.product.exception.LoginFailedException;
import gift.product.model.KakaoToken;
import gift.product.model.Member;
import gift.product.property.KakaoProperties;
import gift.product.repository.AuthRepository;
import gift.product.repository.KakaoTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.NoSuchElementException;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Transactional(readOnly = true)
public class AuthService {

    private static final String KAKAO_AUTH_CODE_BASE_URL = "https://kauth.kakao.com/oauth/authorize?scope=talk_message,account_email&response_type=code";
    private final AuthRepository authRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.builder().build();
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public AuthService(AuthRepository authRepository,
        KakaoTokenRepository kakaoTokenRepository,
        KakaoProperties kakaoProperties) {
        this.authRepository = authRepository;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.kakaoProperties = kakaoProperties;
    }

    @Transactional
    public void register(MemberDto memberDto) {
        validateMemberExist(memberDto);

        Member member = new Member(memberDto.email(), memberDto.password());
        authRepository.save(member);
    }

    public AccessTokenDto login(MemberDto memberDto) {
        validateMemberInfo(memberDto);
        Member member = authRepository.findByEmail(memberDto.email());

        return new AccessTokenDto(getAccessToken(member));
    }

    public String getKakaoAuthCodeUrl() {
        return KAKAO_AUTH_CODE_BASE_URL + "&redirect_uri=" + kakaoProperties.redirectUrl()
            + "&client_id=" + kakaoProperties.clientId();
    }

    public OAuthJwt getOAuthToken(String code, String externalApiUrl) {
        LinkedMultiValueMap<String, String> body = generateBodyForToken(code);

        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(externalApiUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                throw new LoginFailedException("토큰 발급 관련 에러가 발생하였습니다. 다시 시도해주세요.");
            }))
            .toEntity(String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String accessToken = rootNode.path("access_token").asText();
            String refreshToken = rootNode.path("refresh_token").asText();

            return new OAuthJwt(accessToken, refreshToken);
        } catch (Exception e) {
            throw new InternalException("소셜 로그인 진행 중 예기치 못한 오류가 발생하였습니다. 다시 시도해 주세요.");
        }
    }

    public AccessTokenDto registerKakaoMember(OAuthJwt oAuthJwt, String externalApiUrl) {
        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(externalApiUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + oAuthJwt.accessToken())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                throw new LoginFailedException("카카오 유저 정보 조회 관련 에러가 발생하였습니다. 다시 시도해주세요.");
            }))
            .toEntity(String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String memberEmail = rootNode.path("kakao_account").path("email").asText();

            if (!authRepository.existsByEmail(memberEmail)) {
                authRepository.save(new Member(memberEmail, "oauth"));
            }

            return getAccessTokenDto(oAuthJwt, memberEmail);
        } catch (Exception e) {
            throw new InternalException("소셜 로그인 진행 중 예기치 못한 오류가 발생하였습니다. 다시 시도해 주세요.");
        }
    }

    public long unlinkKakaoAccount(LoginMemberIdDto loginMemberIdDto, String externalApiUrl) {
        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(externalApiUrl))
            .header("Authorization", "Bearer " + getKakaoToken(loginMemberIdDto).getAccessToken())
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                throw new LoginFailedException("카카오 유저 연결을 끊는 도중 에러가 발생하였습니다. 다시 시도해주세요.");
            }))
            .toEntity(String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            return Long.parseLong(rootNode.path("id").asText());
        } catch (Exception e) {
            throw new InternalException("소셜 로그인 진행 중 예기치 못한 오류가 발생하였습니다. 다시 시도해 주세요.");
        }
    }

    private String getAccessToken(Member member) {
        String EncodedSecretKey = Encoders.BASE64.encode(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(EncodedSecretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
            .claim("id", member.getId())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L))
            .signWith(key)
            .compact();
    }

    private String getRefreshToken(Member member) {
        String EncodedSecretKey = Encoders.BASE64.encode(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(EncodedSecretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
            .claim("id", member.getId())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 30L))
            .signWith(key)
            .compact();
    }

    private void validateMemberExist(MemberDto memberDto) {
        boolean isMemberExist = authRepository.existsByEmail(memberDto.email());

        if (isMemberExist) {
            throw new DuplicateRequestException("이미 회원으로 등록된 이메일입니다.");
        }
    }

    private void validateMemberInfo(MemberDto memberDto) {
        boolean isMemberExist = authRepository.existsByEmail(memberDto.email());

        if (!isMemberExist) {
            throw new LoginFailedException("회원 정보가 존재하지 않습니다.");
        }

        Member member = authRepository.findByEmail(memberDto.email());

        if (!memberDto.password().equals(member.getPassword())) {
            throw new LoginFailedException("비밀번호가 일치하지 않습니다.");
        }
    }

    private LinkedMultiValueMap<String, String> generateBodyForToken(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", kakaoProperties.grantType());
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUrl());
        body.add("code", code);
        body.add("client_secret", kakaoProperties.clientSecret());
        return body;
    }

    private AccessTokenDto getAccessTokenDto(OAuthJwt oAuthJwt, String memberEmail) {
        Member member = authRepository.findByEmail(memberEmail);

        kakaoTokenRepository.save(new KakaoToken(member.getId(),
            oAuthJwt.accessToken(),
            oAuthJwt.refreshToken()));

        return new AccessTokenDto(getAccessToken(member));
    }

    private KakaoToken getKakaoToken(LoginMemberIdDto loginMemberIdDto) {
        return kakaoTokenRepository.findByMemberId(loginMemberIdDto.id())
            .orElseThrow(() -> new NoSuchElementException("카카오 계정 로그인을 수행한 후 다시 시도해주세요."));
    }
}
