package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoCommerceMessage {

    private final String objectType = "commerce";
    private Content content;
    private Commerce commerce;

    protected KakaoCommerceMessage() {
    }

    public KakaoCommerceMessage(String title, String imageUrl, String description, String webUrl,
        Integer regularPrice) {
        this.content = new Content(title, imageUrl, description, new Link(webUrl));
        this.commerce = new Commerce(regularPrice);
    }

    public String getObjectType() {
        return objectType;
    }

    public Content getContent() {
        return content;
    }

    public Commerce getCommerce() {
        return commerce;
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private class Content {

        private String title;
        private String imageUrl;
        private String description;
        private Link link;

        protected Content() {
        }

        public Content(String title, String imageUrl, String description, Link link) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.description = description;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getDescription() {
            return description;
        }

        public Link getLink() {
            return link;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private class Link {

        private String webUrl;

        protected Link() {
        }

        public Link(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getWebUrl() {
            return webUrl;
        }
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    private class Commerce {

        private Integer regularPrice;

        protected Commerce() {
        }

        public Commerce(Integer regularPrice) {
            this.regularPrice = regularPrice;
        }

        public Integer getRegularPrice() {
            return regularPrice;
        }
    }
}
