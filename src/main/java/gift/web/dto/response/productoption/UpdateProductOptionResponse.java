package gift.web.dto.response.productoption;

import gift.domain.ProductOption;

public class UpdateProductOptionResponse {

    private Long id;
    private String name;
    private Integer quantity;

    public UpdateProductOptionResponse(Long id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static UpdateProductOptionResponse fromEntity(ProductOption option) {
        return new UpdateProductOptionResponse(option.getId(), option.getName(), option.getStock());
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
