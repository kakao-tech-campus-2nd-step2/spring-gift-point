package gift.service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class KakaoMessageTemplate {
    @JsonProperty("object_type")
    private final String objectType;
    private final String text;
    private final Map<String, String> link;

    public KakaoMessageTemplate(String objectType, String text, Map<String, String> link) {
        this.objectType = objectType;
        this.text = text;
        this.link = link;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getText() {
        return text;
    }

    public Map<String, String> getLink() {
        return link;
    }
}