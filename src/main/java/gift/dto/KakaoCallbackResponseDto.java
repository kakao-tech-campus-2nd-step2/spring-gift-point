package gift.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class KakaoCallbackResponseDto {
  private final String jwtToken;
  private final String kakaoAccessToken;
  private JsonNode userInfo;

  public KakaoCallbackResponseDto(String jwtToken, String kakaoAccessToken, JsonNode userInfo) {
    this.jwtToken = jwtToken;
    this.kakaoAccessToken = kakaoAccessToken;
    this.userInfo = userInfo;

  }

  public String getJwtToken() {
    return jwtToken;
  }

  public String getKakaoAccessToken() {
    return kakaoAccessToken;
  }
}