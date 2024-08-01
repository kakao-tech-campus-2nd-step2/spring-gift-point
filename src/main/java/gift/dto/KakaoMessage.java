package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessage(
    @JsonProperty("object_type")
    String objectType,

    String text,

    Link link
) {



}
