package gift.auth.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.util.LinkedMultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TemplateObject(
    String objectType,
    String text,
    LinkObject link,
    String buttonTitle
) {

    public TemplateObject(String objectType, String text, String url, String buttonTitle) {
        this(objectType, text, new LinkObject(url, url), buttonTitle);
    }

    public LinkedMultiValueMap<String, String> makeBody() {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            body.add("template_object", objectMapper.writeValueAsString(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return body;
    }
}
