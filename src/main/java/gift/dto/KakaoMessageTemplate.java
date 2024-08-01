package gift.dto;

public class KakaoMessageTemplate {
    private String objectType = "text";
    private String text;
    private String linkUrl;


    public KakaoMessageTemplate(String text, String linkUrl) {
        this.text = text;
        this.linkUrl = linkUrl;
    }


    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }
}