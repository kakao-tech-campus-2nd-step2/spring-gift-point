package gift.web.dto.response.productoption;

import gift.domain.ProductOption;

public class CreateProductOptionResponse {

    private final Long id;
    private final String name;
    private final Integer quantity;

    public CreateProductOptionResponse(Long id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static CreateProductOptionResponse fromEntity(ProductOption productOption) {
        return new CreateProductOptionResponse(
            productOption.getId(),
            productOption.getName(),
            productOption.getStock()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
