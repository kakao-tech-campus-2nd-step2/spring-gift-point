package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProperties;
import gift.dto.MemberDto;
import gift.model.Member;
import gift.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final String secretKey;

    private final KakaoProperties kakaoProperties;

    public MemberService(MemberRepository memberRepository,
        @Value("${secret.key}") String secretKey, KakaoProperties kakaoProperties) {
        this.memberRepository = memberRepository;
        this.secretKey = secretKey;
        this.kakaoProperties = kakaoProperties;
    }


    public Optional<String> registerMember(Member member) {
        String hashedPassword = BCrypt.hashpw(member.getPassword(), BCrypt.gensalt());
        // BCrypt 알고리즘에서 salt를 생성 -> 해시 함수에 추가되는 랜덤한 값으로 같은 비밀번호라도 솔트가 다르면 다른 해시값을 생성
        // hash password -> 비밀번호와 솔트를 조합하여 BCrypt 해시 함수를 적용, 해시된 비밀번호를 문자열로 반환
        member.setPassword(hashedPassword); // 암호화된 비밀번호를 멤버의 password로 설정!
        Member savedMember = memberRepository.save(member); // 해당 멤버를 memberRepository에 저장

        // 클레임은 토큰에 대한 추가적인 정보를 제공하거나 토큰의 권한, 만료 시간 등을 명시하는 데 사용됩니다

        // Jwt = JSON Web Token
        String token = Jwts.builder() // Jwt Builder 객체 생성 -> Builder Pattern
            .subject(savedMember.getId().toString()) // Jwt의 subject는 saveMember의 ID로 한다
            .claim("email",
                savedMember.getEmail()) // Claim은 토큰에 포함된 정보의 한 조각으로 키-값 쌍으로 이루어짐. 여기서 "email"이라는 키에 savedMember의 이메일 값을 할당
            .signWith(Keys.hmacShaKeyFor(
                secretKey.getBytes())) // Jwt에 서명 추가. 서명은 토큰의 무결성을 보장, 토큰이 변조되지 않았음을 확인. 여기서는 HMAC-SHA 알고리즘을 사용하여 서명 생성. secretKey는 바이트 배열로 변환하여 전달된다.
            .compact(); // Jwt 빌더를 완성하고 압축된 형태의 Jwt 문자열을 생성, 이 문자열이 최종적으로 token 변수에 할당된다.

        return Optional.of(token); // Optional<String>으로 리턴
    }

    public Optional<String> registerKakaoMember(Member member) {
        var hashedKakaoId = BCrypt.hashpw(member.getKakaoId(), BCrypt.gensalt());
        member.setKakaoId(hashedKakaoId);
        var savedKakaoMember = memberRepository.save(member);

        String token = Jwts.builder() // Jwt Builder 객체 생성 -> Builder Pattern
            .subject(savedKakaoMember.getId().toString()) // Jwt의 subject는 saveMember의 ID로 한다
            .claim("kakaoId",
                savedKakaoMember.getKakaoId()) // Claim은 토큰에 포함된 정보의 한 조각으로 키-값 쌍으로 이루어짐. 여기서 "email"이라는 키에 savedMember의 이메일 값을 할당
            .signWith(Keys.hmacShaKeyFor(
                secretKey.getBytes())) // Jwt에 서명 추가. 서명은 토큰의 무결성을 보장, 토큰이 변조되지 않았음을 확인. 여기서는 HMAC-SHA 알고리즘을 사용하여 서명 생성. secretKey는 바이트 배열로 변환하여 전달된다.
            .compact(); // Jwt 빌더를 완성하고 압축된 형태의 Jwt 문자열을 생성, 이 문자열이 최종적으로 token 변수에 할당된다.

        return Optional.of(token); // Optional<String>으로 리턴

    }

    public Optional<String> loginOrRegisterKakaoUser(Long kakaoId, String kakaoEmail) {
        Optional<Member> existingMember = memberRepository.findByKakaoId(String.valueOf(kakaoId));

        Member member;
        if (existingMember.isPresent()) {
            member = existingMember.get();
        } else {
            member = new Member(kakaoEmail, String.valueOf(kakaoId), String.valueOf(kakaoId));
            member = memberRepository.save(member);
        }

        String token = Jwts.builder()
            .subject(member.getId().toString())
            .claim("email", member.getEmail())
            .claim("kakaoId", member.getKakaoId())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        return Optional.of(token);
    }

    public Optional<String> login(String email, String password) {
        Optional<Member> memberOptional = memberRepository.findByEmail(
            email); // Email을 통해 repository에서 멤버 가져옴
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            if (BCrypt.checkpw(password, member.getPassword())) { // BCrypt를 통해 password를 확인한다
                String jws = Jwts.builder()
                    .subject(member.getId().toString())
                    .claim("email", member.getEmail())
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .compact();
                return Optional.of(jws);
            }
        }
        return Optional.empty();
    }

    public Optional<String> kakaoLogin(Long kakaoId) {
        var hashedKakaoId = BCrypt.hashpw(kakaoId.toString(), BCrypt.gensalt());
        var byKakaoId = memberRepository.findByKakaoId(hashedKakaoId);
        if (byKakaoId.isPresent()) {
            var member = byKakaoId.get();
            if (BCrypt.checkpw(String.valueOf(kakaoId), member.getKakaoId())) {
                var jws = Jwts.builder().subject(member.getKakaoId())
                    .claim("kakaoId", member.getKakaoId())
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes())).compact();
                return Optional.of(jws);
            }
        }
        return Optional.empty();
    }

    public Optional<String> checkKakaoToken(String token) {
        return null;
    }



    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }


    public void getAuthentificationToken(@NotNull MemberDto memberDto) {
        var url = "https://kauth.kakao.com/oauth/authorize";
        var headers = new HttpHeaders();
    }

    public StringBuffer getAuthentificationCode() {
        var url = new StringBuffer();
        url.append("https://kauth.kakao.com/oauth/authorize?");
        url.append("client_id=" + kakaoProperties.clientId());
        url.append("&redirect_uri=" + kakaoProperties.redirectUrl());
        url.append("&response_type=code");
        return url;
    }

    public ResponseEntity<String> getResponseEntity(String token) {
        var kakaoTokenRequest = getMultiValueMapHttpEntity(token);
        var restTemplate = new RestTemplate();
        var response =
            restTemplate.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
            );
        System.out.println(response);

        return response;
    }


    private HttpEntity<MultiValueMap<String, String>> getMultiValueMapHttpEntity(String token) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", token);

        return new HttpEntity<MultiValueMap<String, String>>(body, httpHeaders);
    }

    public String getAccessToken(String responseBody) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    public String getClientId(String responseBody) throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("id_token").asText();
    }

}
