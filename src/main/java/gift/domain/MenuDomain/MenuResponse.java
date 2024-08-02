package gift.domain.MenuDomain;

public record MenuResponse(
        Long id,
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {

}
