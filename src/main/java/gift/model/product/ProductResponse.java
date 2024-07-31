package gift.model.product;

public record ProductResponse(Long id, String name, int price, String image_url, Long category_id) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }

}
