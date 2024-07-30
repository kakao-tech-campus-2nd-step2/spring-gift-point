package gift.model.product;

import gift.model.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProductRequest(
    String name,
    int price,
    String imageUrl,
    Long categoryId
) {
    public Product toEntity(Category category, String name, int quantity) {
        return new Product(name, price, imageUrl, category, name, quantity);
    }

}
