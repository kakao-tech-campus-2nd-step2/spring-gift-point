package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoUserInfoDTO(
    @JsonProperty("id")
    Long id,
    @JsonProperty("kakao_account")
    KakaoAccountDTO kakaoAccountDTO
) {

}
