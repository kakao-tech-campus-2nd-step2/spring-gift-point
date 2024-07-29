package gift.domain;

public class SendKakao {

    public static class Message {

        private String objectType = "text";
        private String text;
        private Link link;

        public Message() {
        }

        public Message(String text, Link link) {
            this.text = text;
            this.link = link;
        }

        public String getObjectType() {
            return objectType;
        }

        public String getText() {
            return text;
        }

        public Link getLink() {
            return link;
        }

        @Override
        public String toString() {
            return "{"
                + "\"object_type\": \"" + objectType + "\","
                + "\"text\": \"" + text + "\","
                + "\"link\": {"
                + "\"web_url\": \"" + link.getWebUrl() + "\","
                + "\"mobile_web_url\": \"" + link.getMobileWebUrl() + "\""
                + "}"
                + "}";
        }
    }

    public static class Link {

        private String webUrl;
        private String mobileWebUrl;

        public Link() {
        }

        public Link(String webUrl, String mobileWebUrl) {
            this.webUrl = webUrl;
            this.mobileWebUrl = mobileWebUrl;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public String getMobileWebUrl() {
            return mobileWebUrl;
        }
    }
}
