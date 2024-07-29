package gift.kakao.message.dto;

import org.springframework.util.LinkedMultiValueMap;

public record TemplateObject(
        String objectType,
        String text,
        Link link
) {
    public TemplateObject(String objectType,
                          String text,
                          String url) {
        this(objectType, text, new Link(url));
    }

    public LinkedMultiValueMap<String, String> toRequestBody() {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("template_object", this.toString());
        return requestBody;
    }

    @Override
    public String toString() {
        return "{\"object_type\":\"" + objectType + "\"," +
                "\"text\":\"" + text.replace("\n", "\\n") + "\"," +
                "\"link\":{" +
                "\"web_url\":\"" + link.webUrl() + "\"," +
                "\"mobile_url\":\"" + link.mobileUrl() + "\"}}";
    }
}
