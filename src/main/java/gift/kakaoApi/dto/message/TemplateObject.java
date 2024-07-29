package gift.kakaoApi.dto.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.kakaoApi.exception.KakaoMessageException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateObject(
    String objectType,
    String text,
    Link link

) {

    public MultiValueMap<String, String> toRequestBody() {
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        try {
            map.add("template_object", objectMapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            throw new KakaoMessageException("[메세지 전송 실패] Object 만들기 실패");
        }
        return map;
    }
}
