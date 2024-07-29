package gift.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Link {
    private String webUrl;
    private String mobileWebUrl;

    public Link(String webUrl, String mobileWebUrl) {
        validateWebUrl(webUrl);
        validateMobileUrl(mobileWebUrl);

        this.webUrl = webUrl;
        this.mobileWebUrl = mobileWebUrl;
    }

    private void validateWebUrl(String webUrl) {
        if (webUrl == null || webUrl.isBlank())
            throw new IllegalArgumentException("webUrl은 필수입니다");
    }

    private void validateMobileUrl(String mobileWebUrl) {
        if (mobileWebUrl == null || mobileWebUrl.isBlank())
            throw new IllegalArgumentException("mobileUrl은 필수입니다");
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getMobileWebUrl() {
        return mobileWebUrl;
    }
}
