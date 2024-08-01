package gift.web.dto.response.productoption;

public class ProductOptionResponse {
    private final Long id;
    private final String name;
    private final Long productId;
    private final Integer quantity;

    public ProductOptionResponse(Long id, String name, Long productId, Integer quantity) {
        this.id = id;
        this.name = name;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static ProductOptionResponse fromEntity(gift.domain.ProductOption productOption) {
        return new ProductOptionResponse(productOption.getId(), productOption.getName(), productOption.getProductId(),
            productOption.getStock());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
