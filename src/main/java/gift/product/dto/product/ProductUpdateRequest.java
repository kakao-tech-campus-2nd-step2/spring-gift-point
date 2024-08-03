package gift.product.dto.product;

public interface ProductUpdateRequest {

    String name();

    int price();

    String imageUrl();

    Long categoryId();
}
