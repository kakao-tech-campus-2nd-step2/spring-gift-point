package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    
    private String token;

    @JsonCreator
    public TokenResponse(
        @JsonProperty("token")
        String token
    ){
        this.token =token;
    }

    public String getToken(){
        return token;
    }
}
