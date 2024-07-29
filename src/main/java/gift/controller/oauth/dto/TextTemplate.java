package gift.controller.oauth.dto;

public record TextTemplate(
    String object_type,
    String text,
    Link link
) {
}
