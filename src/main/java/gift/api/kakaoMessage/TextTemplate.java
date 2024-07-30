package gift.api.kakaoMessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TextTemplate(@JsonProperty("object_type") String objectType,
                           String text,
                           Link link) {

}
