package gift.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.ToNumberPolicy;
import gift.dto.KakaoUserInfoDTO;
import gift.dto.LinkDTO;
import gift.dto.MessageTemplateDTO;
import gift.dto.OauthTokenDTO;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@PropertySource("classpath:application-dev.properties")
@Component
public class KakaoApiUtil {

    private final RestClient restClient = RestClient.create();

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${client.id}")
    private String clientId;

    Gson gson = new GsonBuilder()
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .create();

    private OauthTokenDTO getToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", "http://localhost:8080/login/token");
        body.add("code", code);

        var response = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        return Mapper(response);
    }

    //유저의 정보를 받아 DTO 형태로 반환.
    public KakaoUserInfoDTO getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        var response = restClient.get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .toEntity(String.class);

        if (response.getBody() == null) {
            throw new IllegalStateException("잘못된 요청입니다.");
        }

        JsonElement jsonElement = JsonParser.parseString(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        long id = jsonObject.get("id").getAsLong();
        String email = jsonObject.getAsJsonObject("kakao_account").get("email").getAsString();

        return new KakaoUserInfoDTO(id, email, accessToken);

    }

    //카카오톡 나에게 메세지 보내기 API를 이용해 메시지 전송.
    public ResponseEntity<String> SendOrderMessage(String text,String accessToken){

        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        MessageTemplateDTO messageTemplate = new MessageTemplateDTO("text"
            ,text,new LinkDTO("http://localhost:8080","http://localhost:8080"));

        String messageTemplateJson = gson.toJson(messageTemplate);
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", messageTemplateJson);


        var response = restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization","Bearer "+accessToken)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        return response;
    }

    private OauthTokenDTO Mapper(ResponseEntity<String> response) {
        return gson.fromJson(response.getBody(), OauthTokenDTO.class);
    }

    public String getAccessToken(String code) {
        OauthTokenDTO token = getToken(code);
        return token.getAccessToken();

    }

}
