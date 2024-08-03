package gift.dto;

public class KakaoCallbackResponseDto {
  private final String jwtToken;
  private final String kakaoAccessToken;

  public KakaoCallbackResponseDto(String jwtToken, String kakaoAccessToken) {
    this.jwtToken = jwtToken;
    this.kakaoAccessToken = kakaoAccessToken;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public String getKakaoAccessToken() {
    return kakaoAccessToken;
  }
}