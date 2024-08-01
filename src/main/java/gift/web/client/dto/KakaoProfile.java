package gift.web.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class KakaoProfile {

    private String nickname;

    @JsonCreator
    public KakaoProfile(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
