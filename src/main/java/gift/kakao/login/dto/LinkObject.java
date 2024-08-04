package gift.kakao.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkObject {
    @JsonProperty("web_url")
    private String webUrl;

    public LinkObject(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }
}
