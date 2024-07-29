package gift.product.business.dto;

public class KakaoOrderMessage {

    public record Link(
        String web_url
    ) {

    }

    public record Content(
        String title,
        Link link
    ) {

    }

    public record Commerce(
        int regular_price
    ) {

    }

    public record TemplateObject(
        String object_type,
        Content content,
        Commerce commerce
    ) {

        public static TemplateObject of(String title, String webUrl, int regularPrice) {
            return new TemplateObject(
                "commerce",
                new Content(title, new Link(webUrl)),
                new Commerce(regularPrice)
            );
        }

    }

}
