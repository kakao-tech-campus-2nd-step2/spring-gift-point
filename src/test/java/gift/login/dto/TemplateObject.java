package gift.kakao.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateObject {
    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("text")
    private String text;

    @JsonProperty("link")
    private LinkObject link;

    public TemplateObject(String objectType, String text, LinkObject link) {
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

    public LinkObject getLink() {
        return link;
    }
}
