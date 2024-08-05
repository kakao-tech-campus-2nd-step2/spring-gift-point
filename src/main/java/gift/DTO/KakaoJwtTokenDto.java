package gift.DTO;

public class KakaoJwtTokenDto {

  private Long id;
  private final String accessToken;
  private final String refreshToken;

  public KakaoJwtTokenDto(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public KakaoJwtTokenDto(Long id, String accessToken, String refreshToken) {
    this.id = id;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public Long getId() {
    return this.id;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

}
