package gift.dto;

public class LinkDTO {

    private String web_url;
    private String mobile_web_url;

    public LinkDTO(String web_url, String mobile_web_url) {
        this.web_url = web_url;
        this.mobile_web_url = mobile_web_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public String getMobile_web_url() {
        return mobile_web_url;
    }
}
