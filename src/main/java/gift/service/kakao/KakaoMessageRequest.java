package gift.service.kakao;

public class KakaoMessageRequest {

    private String object_type;
    private String text;
    private Link link;

    public KakaoMessageRequest(String object_type, String text, Link link) {
        this.object_type = object_type;
        this.text = text;
        this.link = link;
    }

    public String getObject_type() {
        return object_type;
    }

    public String getText() {
        return text;
    }

    public Link getLink() {
        return link;
    }

    public static class Link {
        private String web_url;

        public Link(String web_url) {
            this.web_url = web_url;
        }

        public String getWeb_url() {
            return web_url;
        }

    }

}
