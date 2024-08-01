package gift.web.dto.response.productoption;

import gift.domain.ProductOption;

public class UpdateProductOptionResponse {

    private Long id;
    private String name;
    private Integer stock;

    public UpdateProductOptionResponse(Long id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
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

    public Integer getStock() {
        return stock;
    }

}
