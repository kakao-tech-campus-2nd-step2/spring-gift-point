package gift.domain;

import com.fasterxml.jackson.databind.JsonNode;
import gift.exception.customException.JsonException;

import static gift.utils.TokenConstant.*;

public class TokenInformation {

    private final JsonNode jsonNode;

    public TokenInformation(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
        setInformation();
    }

    private String accessToken;

    private Integer accessTokenTime;

    private String refreshToken;

    private Integer refreshTokenTime;

    private void setAccessToken(){
        if (!jsonNode.has(ACCESS_TOKEN)){
            throw new JsonException();
        }
        this.accessToken = jsonNode.get(ACCESS_TOKEN).asText();
    }

    private void setAccessTokenTime(){
        if (!jsonNode.has(EXPIRES_IN)){
            throw new JsonException();
        }
        this.accessTokenTime = jsonNode.get(EXPIRES_IN).asInt();
    }

    private void setRefreshToken(){
        if (jsonNode.has(REFRESH_TOKEN)){
            this.refreshToken = jsonNode.get(REFRESH_TOKEN).asText();
        }
    }

    private void setRefreshTokenTime(){
        if (jsonNode.has(REFRESH_TOKEN_EXPIRES_IN)){
            this.refreshTokenTime = jsonNode.get(REFRESH_TOKEN_EXPIRES_IN).asInt();
        }
    }

    public void setInformation(){
        setAccessToken();
        setAccessTokenTime();
        setRefreshToken();
        setRefreshTokenTime();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getAccessTokenTime() {
        return accessTokenTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getRefreshTokenTime() {
        return refreshTokenTime;
    }
}
