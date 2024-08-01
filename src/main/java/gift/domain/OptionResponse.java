package gift.domain;

public record OptionResponse(
        Long id,
        String name,
        Long quantity,
        Menu menu
) {
}
