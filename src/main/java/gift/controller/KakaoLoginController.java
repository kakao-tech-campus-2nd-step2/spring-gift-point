package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.ApiResponse;
import gift.model.HttpResult;
import gift.service.MemberService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
public class KakaoLoginController {

    private MemberService memberService;
    private String accessToken;
    private String authCode;

    public KakaoLoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String redirectedPageForAuthCode(
        @RequestParam(value = "code", required = false) String code,
        Model model, HttpSession session) {
        model.addAttribute("message", "homepage");
        if (code != null) {
            session.setAttribute("kakao_auth_code", code);
            authCode = code;
            model.addAttribute("message", "인가 코드 확인");
            log.info("This is an info code {}",code);
            return "redirect:/kakao/login";
        }
        return "kakaologin";
    }

    @GetMapping("/kakao/page")
    public String getKakaoPage() {
        return "kakaologin";
    }

    @GetMapping("/kakao/authcode")
    public String getAuthentificationCodeWithKakao(
    ) {
        return "redirect:" + memberService.getAuthentificationCode();
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<ApiResponse> loginWithKakao(HttpSession session) throws JsonProcessingException {
        String authCode = (String) session.getAttribute("kakao_auth_code");
        if (authCode == null) {
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "인가 코드 에러", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<String> response = memberService.getResponseEntity(authCode);
        String accessToken = memberService.getAccessToken(response.getBody());
        System.out.println(accessToken);
        var headers = getHttpHeaders(accessToken);
        var responseEntity = getHttpResponse(headers);

        Long kakaoId = getKakaoId(responseEntity);
        String kakaoEmail = getKakaoEmail(responseEntity);

        try {
            Optional<String> jwtToken = memberService.loginOrRegisterKakaoUser(kakaoId, kakaoEmail);
            if (jwtToken.isPresent()) {
                ApiResponse apiResponse = new ApiResponse(HttpResult.OK, "카카오 로그인 성공", HttpStatus.OK);
                System.out.println(jwtToken.get());
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                    new ApiResponse(HttpResult.ERROR, "카카오 로그인 실패", HttpStatus.UNAUTHORIZED),
                    HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(
                new ApiResponse(HttpResult.ERROR, "카카오 로그인 처리 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/kakao/gift/self")
    public ResponseEntity<ApiResponse> giftForOneSelf(HttpSession session)
        throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Create the template object as a JSON string
        var templateObject = getString();

        MultiValueMap<String, String> bodyTemplate = new LinkedMultiValueMap<>();
        bodyTemplate.add("template_object", templateObject);

        var headers = getHttpHeadersForSelfMessage(accessToken);
        var responseEntity = getHttpSendSelfMessage(bodyTemplate, headers);

        String responseBody = responseEntity.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        var resultCode = jsonNode.get("result_code").asText();
        if (resultCode.equals("0")) {
            return new ResponseEntity<>(new ApiResponse(HttpResult.OK, "성공", HttpStatus.OK),
                HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse(HttpResult.ERROR, "실패", HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST);
    }

    private static String getString() {
        StringBuilder templateObject = new StringBuilder();
        templateObject.append("{")
            .append("\"object_type\": \"feed\",")
            .append("\"content\": {")
            .append("  \"title\": \"오늘의 디저트\",")
            .append("  \"description\": \"아메리카노, 빵, 케익\",")
            .append("  \"image_url\": \"https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg\",")
            .append("  \"image_width\": 640,")
            .append("  \"image_height\": 640,")
            .append("  \"link\": {")
            .append("    \"web_url\": \"http://www.daum.net\",")
            .append("    \"mobile_web_url\": \"http://m.daum.net\",")
            .append("    \"android_execution_params\": \"contentId=100\",")
            .append("    \"ios_execution_params\": \"contentId=100\"")
            .append("  }")
            .append("},")
            .append("\"item_content\": {")
            .append("  \"profile_text\": \"Kakao\",")
            .append("  \"profile_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\",")
            .append("  \"title_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\",")
            .append("  \"title_image_text\": \"Cheese cake\",")
            .append("  \"title_image_category\": \"Cake\",")
            .append("  \"items\": [")
            .append("    {\"item\": \"Cake1\", \"item_op\": \"1000원\"},")
            .append("    {\"item\": \"Cake2\", \"item_op\": \"2000원\"},")
            .append("    {\"item\": \"Cake3\", \"item_op\": \"3000원\"},")
            .append("    {\"item\": \"Cake4\", \"item_op\": \"4000원\"},")
            .append("    {\"item\": \"Cake5\", \"item_op\": \"5000원\"}")
            .append("  ],")
            .append("  \"sum\": \"Total\",")
            .append("  \"sum_op\": \"15000원\"")
            .append("},")
            .append("\"social\": {")
            .append("  \"like_count\": 100,")
            .append("  \"comment_count\": 200,")
            .append("  \"shared_count\": 300,")
            .append("  \"view_count\": 400,")
            .append("  \"subscriber_count\": 500")
            .append("},")
            .append("\"buttons\": [")
            .append("  {")
            .append("    \"title\": \"웹으로 이동\",")
            .append("    \"link\": {")
            .append("      \"web_url\": \"http://www.daum.net\",")
            .append("      \"mobile_web_url\": \"http://m.daum.net\"")
            .append("    }")
            .append("  },")
            .append("  {")
            .append("    \"title\": \"앱으로 이동\",")
            .append("    \"link\": {")
            .append("      \"android_execution_params\": \"contentId=100\",")
            .append("      \"ios_execution_params\": \"contentId=100\"")
            .append("    }")
            .append("  }")
            .append("]")
            .append("}");
        return templateObject.toString();
    }
    private Long getKakaoId(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("id").asLong();
    }

    private String getKakaoEmail(ResponseEntity<String> response) throws JsonProcessingException {
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("kakao_account").get("email").toString().replaceAll("\"", "");
    }

    public HttpHeaders getHttpHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    public HttpHeaders getHttpHeadersForSelfMessage(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public ResponseEntity<String> getHttpResponse(HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }

    public ResponseEntity<String> getHttpSendSelfMessage(MultiValueMap<String, String> body,
        HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(
            body, headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }
}
