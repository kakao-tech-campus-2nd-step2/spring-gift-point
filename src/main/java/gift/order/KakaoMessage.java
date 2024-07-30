package gift.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.order.KakaoMessage.TemplateObject.Link;
import org.springframework.util.LinkedMultiValueMap;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMessage (
    TemplateObject templateObject
) {
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TemplateObject (
        String objectType,
        String text,
        Link link,
        String buttonTitle
    ) {
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        record Link(String webUrl, String mobileWebUrl){ }
    }

    public static LinkedMultiValueMap<String, String> createBody(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Link link = new Link("https://example.com", "https://example.com");
        TemplateObject templateObject = new TemplateObject("text", message, link, "버튼");

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        try {
            String jsonTemplateObject = mapper.writeValueAsString(templateObject);
            body.add("template_object", jsonTemplateObject);
        } catch (JsonProcessingException e) {
            System.err.println("Serialize template_object 에러: " + e.getMessage());
        }

        return body;
    }
}
