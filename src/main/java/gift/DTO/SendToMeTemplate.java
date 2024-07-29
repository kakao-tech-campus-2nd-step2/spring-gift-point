package gift.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendToMeTemplate {
    private String objectType;
    private String text;
    @JsonProperty("link")
    private Link link;


    public SendToMeTemplate(String objectType, String text, Link link) {
        validateObjectType(objectType);
        validateLink(link);

        this.objectType = objectType;
        this.text = text;
        this.link = link;
    }

    private void validateObjectType(String objectType) {
        if (objectType == null || objectType.isBlank())
            throw new IllegalArgumentException("ObjectType은 필수입니다");
    }

    private void validateLink(Link link) {
        if (link == null)
            throw new IllegalArgumentException("link는 필수입니다");
    }

    public String getObjectType() {
        return objectType;
    }

    public String getText() {
        return text;
    }

    public Link getLink() {
        return link;
    }
}
