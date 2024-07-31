package gift.web.client.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.web.dto.response.product.ReadProductResponse;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoCommerce {

    private final String objectType = "commerce";
    private Content content;
    private Commerce commerce;

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class Content {
        private String title;
        private String imageUrl;
        private Integer imageWidth;
        private Integer imageHeight;
        private String description;
        private Link link;

        public Content(String title, String imageUrl, Integer imageWidth, Integer imageHeight,
            String description, Link link) {
            this.title = title;
            this.imageUrl = imageUrl;
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            this.description = description;
            this.link = link;
        }

        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        static class Link {
            private String webUrl;
            private String mobileWebUrl;
            private String androidExecutionParams;
            private String iosExecutionParams;

            public Link(String webUrl, String mobileWebUrl, String androidExecutionParams,
                String iosExecutionParams) {
                this.webUrl = webUrl;
                this.mobileWebUrl = mobileWebUrl;
                this.androidExecutionParams = androidExecutionParams;
                this.iosExecutionParams = iosExecutionParams;
            }

            public String getWebUrl() {
                return webUrl;
            }

            public String getMobileWebUrl() {
                return mobileWebUrl;
            }

            public String getAndroidExecutionParams() {
                return androidExecutionParams;
            }

            public String getIosExecutionParams() {
                return iosExecutionParams;
            }
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Integer getImageWidth() {
            return imageWidth;
        }

        public Integer getImageHeight() {
            return imageHeight;
        }

        public String getDescription() {
            return description;
        }

        public Link getLink() {
            return link;
        }
    }

    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class Commerce {
        private Integer regularPrice;
        private Integer discountPrice;
        private Integer discountRate;

        public Commerce(Integer regularPrice, Integer discountPrice, Integer discountRate) {
            this.regularPrice = regularPrice;
            this.discountPrice = discountPrice;
            this.discountRate = discountRate;
        }

        public Commerce(Integer regularPrice) {
            this.regularPrice = regularPrice;
        }

        public Integer getRegularPrice() {
            return regularPrice;
        }

        public Integer getDiscountPrice() {
            return discountPrice;
        }

        public Integer getDiscountRate() {
            return discountRate;
        }
    }

    public KakaoCommerce() {

    }

    public KakaoCommerce setCommerce(Integer regularPrice, Integer discountPrice, Integer discountRate) {
        this.commerce = new Commerce(regularPrice, discountPrice, discountRate);
        return this;
    }

    public KakaoCommerce setCommerce(Integer regularPrice) {
        this.commerce = new Commerce(regularPrice);
        return this;
    }

    public KakaoCommerce setContent(String title, String imageUrl, Integer imageWidth, Integer imageHeight, String description, String webUrl, String mobileWebUrl, String androidExecutionParams, String iosExecutionParams) {
        this.content = new Content(title, imageUrl, imageWidth, imageHeight, description,
            new Content.Link(webUrl, mobileWebUrl, androidExecutionParams, iosExecutionParams));
        return this;
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

    /**
     * 카카오 상거래 메시지 생성 - 할인 적용
     * @param product 상품 정보
     * @param discountRate 할인율 (0 ~ 100)
     * @param message 메시지
     * @return
     */
    public static KakaoCommerce of(ReadProductResponse product, int discountRate, String message) {
        return new KakaoCommerce().setContent(
            product.getName(),
            product.getImageUrl().toString(),
            640,
            640,
            message,
            "https://localhost:8080",
            "https://localhost:8080",
            "contentId=" + product.getId(),
            "contentId=" + product.getId()
        ).setCommerce(product.getPrice(), product.getPrice(), 0);
    }

    /**
     * 카카오 상거래 메시지 생성 - 할인 없음
     * @param product 상품 정보
     * @param message 메시지
     * @return
     */
    public static KakaoCommerce of(ReadProductResponse product, String message) {
        return new KakaoCommerce().setContent(
            product.getName(),
            product.getImageUrl().toString(),
            640,
            640,
            message,
            "https://localhost:8080",
            "https://localhost:8080",
            "contentId=" + product.getId(),
            "contentId=" + product.getId()
        ).setCommerce(product.getPrice());
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return "template_object=" + mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    public String getContentDescription() {
        return content.getDescription();
    }
}

