package study;//package study;
//
//import static com.jayway.jsonpath.internal.function.ParamType.JSON;
//import static io.jsonwebtoken.Header.CONTENT_TYPE;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.net.URI;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//public class RestTemplateTest {
//
//    private final RestTemplate client = new RestTemplateBuilder().build();
//
//    @Test
//    void test1() {
//        var url = "https://kauth.kakao.com/oauth/token";
//        var headers = new HttpHeaders();
//        headers.add(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "f1af4bbe948114fcd10611ccaff2928a");
//        body.add("redirect_uri", "http://localhost:8080");
//        body.add("code",
//            "aIB_dTjaY4rclFFCWUPAPThpE8PI83mdkfwrTA4sDeoJiA1zNmvpBwAAAAQKPCRZAAABkOCahYJPBWDH3LuH7A");
//        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
//        var response = client.exchange(request, String.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
//    }
//
//    @Test
//    void 사용자_정보_가져오기() {
//        var url = "https://kapi.kakao.com/v2/user/me";
//        var headers = new HttpHeaders();
//        headers.add("Authorization",
//            "Bearer ZppmY76Yrk5BMRM0ze-XPW_E8Za7zKCdAAAAAQo8JCEAAAGQ4JuC9dQ0RDl69jWm");
//        headers.add(CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//
//        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));
//        ResponseEntity<JsonNode> response = client.exchange(request, JsonNode.class);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
//        System.out.println(response.getBody());
//        System.out.println(response.getBody().get("kakao_account"));
//        System.out.println(response.getBody().get("kakao_account").get("profile_nickname"));
//
//    }
//}
