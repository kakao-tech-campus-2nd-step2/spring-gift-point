package gift.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetKakaoTokenInformation {

    private long id;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("app_id")
    private int appId;

    public long getId() {
        return id;
    }
}
