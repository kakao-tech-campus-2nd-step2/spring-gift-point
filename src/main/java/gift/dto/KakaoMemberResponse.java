package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoMemberResponse {
    @JsonProperty("id")
    private Long id;

    public Long getId() {
        return id;
    }
}
