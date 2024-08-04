package gift.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
    @JsonProperty("access_token") String accessToken
) {
//    private final String access_token;
//
//    public AuthenticationResponse(String accessToken) {
//        this.access_token = accessToken;
//    }
//
//    // getters
//    public String getAccessToken() {
//        return access_token;
//    }
}
