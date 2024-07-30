package gift.external.api.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record FeedObjectRequest(
    String objectType,
    Content content,
    Button[] buttons
) {
    public record Content(
        String title,
        String imageUrl,
        String description,
        Link link
    ) {}

    public record Button(
        String title,
        Link link
    ) {}

    public record Link(
        String webUrl
    ) {}
}
