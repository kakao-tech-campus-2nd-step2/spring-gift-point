package gift.domain.MenuDomain;

public record MenuUpdateRequest(
        String name,
        int price,
        String imageUrl,
        Long categoryId
) {
}
