package gift.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Model.KakaoAccessTokenDto;
import gift.Model.KakaoMemberDto;
import gift.Model.MemberDto;
import gift.Service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@RestController
@Tag(name = "Kakao Login", description = "카카오 로그인 관련 api")
public class KakaoOAuthController {

    @Value("${kakao.clientId}")
    private String clientId;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/oauth/authorize")
    @Operation(summary = "카카오 로그인 화면", description = "카카오 로그인 화면을 보여줍니다.")
    public void authorize(HttpServletResponse response) throws IOException {
        var url = "https://kauth.kakao.com/oauth/authorize";
        var redirectUri = "http://localhost:8080/auth/kakao/callback";
        var responseType = "code";

        String redirectUrl = url + "?response_type=" + responseType + "&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/auth/kakao/callback")
    @Operation(summary = "카카오 로그인 수행", description = "인가 코드를 통해 로그인을 수행합니다.")
    public ResponseEntity<?> callBack(@RequestParam("code") String code, HttpServletResponse response, HttpServletRequest request) throws Exception {
        //인가 코드로 토큰 받아오기
        ResponseEntity<String> accessToken = getAccessToken(code);

        if (accessToken.getStatusCode() != HttpStatus.OK) {
            return accessToken;
        }

        String validAccessToken = accessToken.getBody();

        //토큰을 쿠키에 저장하기
        Cookie cookie = new Cookie("token", validAccessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        //토큰을 통해 사용자 정보 받아오기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + validAccessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoMemberDto kakaoMemberDto = objectMapper.readValue(responseEntity.getBody(), KakaoMemberDto.class);
        String email = kakaoMemberDto.getKakaoAccount().getEmail();

        request.getSession().setAttribute("email", email);

        //이미 가입된 회원인지 확인, 가입되지 않은 회원이라면 회원가입 진행
        if (memberService.findByEmail(email).isEmpty()) {
            MemberDto memberDto = new MemberDto();
            memberDto.setEmail(email);
            memberService.register(memberDto);
        }

        //로그인 처리
        headers = new HttpHeaders();
        headers.add("Location", "/products");

        return new ResponseEntity<>("Successfully logged in", headers, HttpStatus.SEE_OTHER);

    }

    @Operation(summary = "토큰 발급", description = "토큰을 발급받습니다.")
    public ResponseEntity<String> getAccessToken(String code) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers); //순서가 body가 먼저

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                requestEntity,
                String.class
        ); //Http 요청 보내고 받기

        HttpStatusCode statusCode = response.getStatusCode();

        if (statusCode != HttpStatus.OK) {
            return ResponseEntity.status(statusCode).body(statusCode + ": Failed to retrieve access token");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoAccessTokenDto kakaoAccessTokenDto = objectMapper.readValue(response.getBody(), KakaoAccessTokenDto.class);
        return ResponseEntity.ok(kakaoAccessTokenDto.getAccess_token());
    }
}
