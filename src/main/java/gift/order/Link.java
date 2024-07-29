package gift.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {
    @JsonProperty("web_url")
    String webUrl;
    @JsonProperty("mobile_web_url")
    String mobileWebUrl;

    public Link(String webUrl, String mobileWebUrl) {
        this.webUrl = webUrl;
        this.mobileWebUrl = mobileWebUrl;
    }

}
