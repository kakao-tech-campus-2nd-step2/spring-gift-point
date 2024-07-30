package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageTemplate {
    @JsonProperty("object_type")
    private final String objectType;
    private final String text;
    private final Link link;

    private MessageTemplate(Builder builder) {
        this.objectType = "text";
        this.text = builder.text;
        this.link = builder.link;
    }

    public static class Builder {
        private final String text;
        private Link link;

        public Builder(String text) {
            this.text = text;
            this.link = new Link("https://developers.kakao.com");
        }

        public Builder link(String webUrl) {
            this.link = new Link(webUrl);
            return this;
        }

        public MessageTemplate build() {
            return new MessageTemplate(this);
        }
    }

    public static class Link {
        @JsonProperty("web_url")
        private final String webUrl;

        public Link(String webUrl) {
            this.webUrl = webUrl;
        }
    }
}

