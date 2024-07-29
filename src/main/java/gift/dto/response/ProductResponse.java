package gift.dto.response;

import gift.domain.Product;

public record ProductResponse(Long id, String name, int price, String imageUrl, CategoryResponse categoryResponse) {
    public static ProductResponse from(final Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), CategoryResponse.from(product.getCategory()));
    }

    public Product toEntity(){
        return new Product(this.id, this.name, this.price, this.imageUrl, this.categoryResponse.toEntity());
    }
}
