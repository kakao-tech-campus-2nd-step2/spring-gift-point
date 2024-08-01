package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoAccountDTO(
    @JsonProperty("email")
    String email
) {

}
