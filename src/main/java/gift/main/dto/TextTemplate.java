package gift.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TextTemplate(
        @JsonProperty("object_type")
        String objectType,
        String text,
        Link link
) {

    public TextTemplate(@JsonProperty("object_type")
                        String objectType, String text, String link) {
        this(objectType, text, new Link(link));
    }

    record Link(String wep_url) {
    }
}
