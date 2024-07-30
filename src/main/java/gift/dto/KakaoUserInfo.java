package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfo {

    private Long id;

    protected KakaoUserInfo() {
    }

    public Long getId() {
        return id;
    }
}