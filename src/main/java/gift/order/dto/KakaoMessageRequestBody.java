package gift.order.dto;

public record KakaoMessageRequestBody(
    String templateId,
    TemplateArgs templateArgs
) {

}
