package gift.DTO;

public class JwtToken {

  private final String token;

  public JwtToken(String token) {
    this.token = token;
  }

  public String getAccessToken() {
    return this.token;
  }

}