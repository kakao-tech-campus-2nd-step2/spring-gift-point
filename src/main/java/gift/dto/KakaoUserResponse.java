package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserResponse {

    private final Long id;
    private final Properties properties;

    @JsonCreator
    public KakaoUserResponse(@JsonProperty("id") Long id,
                             @JsonProperty("properties") Properties properties) {
        this.id = id;
        this.properties = properties;
    }

    public Long getId() {
        return id;
    }

    public Properties getProperties() {
        return properties;
    }

    public static class Properties {
        private final String nickname;

        @JsonCreator
        public Properties(@JsonProperty("nickname") String nickname) {
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }
    }
}
