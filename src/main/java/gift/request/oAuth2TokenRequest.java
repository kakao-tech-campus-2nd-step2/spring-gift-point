package gift.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record oAuth2TokenRequest(

    @JsonProperty("grant_type")
    String grantType,
    @JsonProperty("client_id")
    String clientId,
    @JsonProperty("redirect_uri")
    String redirectUri,
    String code
) {

}
