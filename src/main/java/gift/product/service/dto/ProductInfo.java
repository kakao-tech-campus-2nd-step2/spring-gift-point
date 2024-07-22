package gift.product.service.dto;

import gift.product.domain.Product;

public record ProductInfo(
        Long id,
        String name,
        Integer price,
        String imgUrl,
        CategoryInfo category
) {
    public static ProductInfo from(Product product) {
        var category = CategoryInfo.from(product.getCategory());

        return new ProductInfo(product.getId(), product.getName(), product.getPrice(), product.getImgUrl(), category);
    }
}
