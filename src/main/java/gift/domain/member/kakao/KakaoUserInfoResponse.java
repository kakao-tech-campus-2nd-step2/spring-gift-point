package gift.domain.member.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoResponse(
    @JsonProperty("id")
    Long id
) {

}
