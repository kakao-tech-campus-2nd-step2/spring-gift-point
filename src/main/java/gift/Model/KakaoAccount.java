package gift.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccount {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
