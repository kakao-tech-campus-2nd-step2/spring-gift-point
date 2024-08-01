package gift.domain.Menu;

public record MenuResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {

}
