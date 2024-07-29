package gift.product;

public record ProductResponse (Long id, String name, Long price, String imageUrl, Long categoryID) {

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

    public ProductResponse() {
        this(null, null, null, null,null);
    }
}
