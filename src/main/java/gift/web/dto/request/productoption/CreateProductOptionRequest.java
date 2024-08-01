package gift.web.dto.request.productoption;

import gift.domain.ProductOption;
import gift.web.validation.constraints.SpecialCharacter;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class CreateProductOptionRequest {

    @NotBlank
    @Length(min = 1, max = 50)
    @SpecialCharacter(allowed = "(, ), [, ], +, -, &, /, _")
    private String name;

    @Range(min = 1, max = 100_000_000)
    private Integer stock;

    public CreateProductOptionRequest(String name, Integer stock) {
        this.name = name;
        this.stock = stock;
    }

    public ProductOption toEntity() {
        return new ProductOption.Builder()
            .name(this.name)
            .stock(this.stock)
            .build();
    }

    public ProductOption toEntity(Long productId) {
        return new ProductOption.Builder()
            .name(this.name)
            .stock(this.stock)
            .productId(productId)
            .build();
    }

    public String getName() {
        return name;
    }

    public Integer getStock() {
        return stock;
    }

}
