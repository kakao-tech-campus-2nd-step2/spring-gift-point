package gift.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
