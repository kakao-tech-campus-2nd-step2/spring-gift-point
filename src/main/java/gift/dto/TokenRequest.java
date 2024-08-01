package gift.dto;

public class TokenRequest {
  private final String grantType;
  private final String clientId;
  private final String redirectUri;
  private final String code;

  public TokenRequest(String grantType, String clientId, String redirectUri, String code) {
    this.grantType = grantType;
    this.clientId = clientId;
    this.redirectUri = redirectUri;
    this.code = code;
  }

  public String getGrantType() {
    return grantType;
  }

  public String getClientId() {
    return clientId;
  }

  public String getRedirectUri() {
    return redirectUri;
  }

  public String getCode() {
    return code;
  }
}
