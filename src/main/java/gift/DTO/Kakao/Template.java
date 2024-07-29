package gift.DTO.Kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Template {
    private String objectType;
    private Content content;
    private Commerce commerce;

    public Template(String objectType, Content content, Commerce commerce) {
        this.objectType = objectType;
        this.content = content;
        this.commerce = commerce;
    }

    public String getObject_type() {
        return objectType;
    }

    public Content getContent() {
        return content;
    }

    public Commerce getCommerce() {
        return commerce;
    }
}
