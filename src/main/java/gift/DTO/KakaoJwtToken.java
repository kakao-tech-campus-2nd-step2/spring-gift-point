package gift.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table
@Entity
public class KakaoJwtToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String accessToken;
  private String refreshToken;

  public KakaoJwtToken(Long id, String accessToken, String refreshToken) {
    this.id = id;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public KakaoJwtToken(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public KakaoJwtToken() {

  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

}
