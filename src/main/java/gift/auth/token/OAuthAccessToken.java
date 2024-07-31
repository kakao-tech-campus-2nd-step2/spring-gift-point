package gift.auth.token;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "accessToken")
public class OAuthAccessToken {
    @Id
    private String username;
    private String tokenType;
    private String accessToken;
    private String issuer;

    @TimeToLive
    private Long expiresIn;

    public OAuthAccessToken(String username, String tokenType, String accessToken, String issuer, Long expiresIn) {
        this.username = username;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.issuer = issuer;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return tokenType.concat(" ").concat(accessToken);
    }
}
