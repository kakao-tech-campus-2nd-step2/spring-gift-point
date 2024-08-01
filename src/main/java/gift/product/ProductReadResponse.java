package gift.product;

import gift.category.CategoryResponse;

public record ProductReadResponse(Long id, String name, Long price, String imageUrl, CategoryResponse category) {

    public ProductReadResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), new CategoryResponse(product.getCategory()));
    }

}
