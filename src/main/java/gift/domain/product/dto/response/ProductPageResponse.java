package gift.domain.product.dto.response;

import java.util.List;

public record ProductPageResponse(
    boolean hasNext,
    List<ProductForPage> products

) {
    public record ProductForPage(
        Long id,
        String name,
        int price,
        String description,
        String imageUrl
    ){

    }
}
