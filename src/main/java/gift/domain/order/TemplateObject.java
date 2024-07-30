package gift.domain.order;

public record TemplateObject(
    String objectType,
    String text,
    Link link,
    String buttonTitle
) {

    public TemplateObject(String text) {
        this(
            "text",
            text,
            new TemplateObject.Link(
                "https://developers.kakao.com",
                "https://developers.kakao.com"
            ),
            "check button");
    }

    public record Link(
        String webUrl,
        String mobileWebUrl
    ) {

    }
}
