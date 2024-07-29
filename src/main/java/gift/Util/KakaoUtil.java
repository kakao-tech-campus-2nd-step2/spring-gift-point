package gift.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.Link;
import gift.DTO.SendToMeTemplate;
import gift.Exception.JsonRunTimeException;
import gift.Exception.KaKaoBadRequestException;
import gift.Exception.KaKaoServerErrorException;
import gift.Model.Value.AccessToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class KakaoUtil {
    private final RestClient client;
    private final KakaoProperties properties;

    public KakaoUtil(RestClient client, KakaoProperties kakaoProperties) {
        this.client = client;
        this.properties = kakaoProperties;
    }

    public void sendMessageToMe(AccessToken accessToken, String message){
        LinkedMultiValueMap<String, String> body = generateBodyOfSendMessageToMe(message);
        client.post().
                uri(URI.create(properties.messageToMeUri()))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getValue()).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new KaKaoBadRequestException("카카오 나에게 메세지 보내기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new KaKaoServerErrorException("카카오 나에게 메세지 보내기 API : " + response.getStatusCode() + "에러 발생. ");
                })
                .toEntity(String.class);
    }

    private LinkedMultiValueMap<String, String> generateBodyOfSendMessageToMe(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        SendToMeTemplate templateObject = new SendToMeTemplate(
                "text",
                message,
                new Link(properties.messageToMeWebUri(), properties.messageToMeUri()));
        try {
            body.add("template_object", objectMapper.writeValueAsString(templateObject));
        } catch (JsonProcessingException e){
            throw new JsonRunTimeException("객체를 Json문자열로 변환 중 에러 발생");
        }
        return body;
    }
}
