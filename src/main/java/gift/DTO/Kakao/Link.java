package gift.DTO.Kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Link {
    private String webUrl;

    public Link(String web_url) {
        this.webUrl = web_url;
    }

    public String getWeb_url() {
        return webUrl;
    }
}
