package gift.web.dto.response.wishproduct;

import gift.domain.Product;
import gift.domain.WishProduct;

public class ReadWishProductResponseByPromise {

    private final Long id;
    private final ProductDto product;

    public ReadWishProductResponseByPromise(Long id, ProductDto productDto) {
        this.id = id;
        this.product = productDto;
    }

    public static ReadWishProductResponseByPromise fromEntity(WishProduct wishProduct) {
        Product product = wishProduct.getProduct();
        return new ReadWishProductResponseByPromise(wishProduct.getId(), ProductDto.from(product));
    }

    public Long getId() {
        return id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public static class ProductDto {

        private final Long id;
        private final String name;
        private final Integer price;
        private final String imageUrl;
        private final Long categoryId;

        public ProductDto(Long id, String name, Integer price, String imageUrl, Long categoryId) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
            this.categoryId = categoryId;
        }

        public static ProductDto from(Product product) {
            return new ProductDto(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl().toString(), product.getCategory().getId());
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Long getCategoryId() {
            return categoryId;
        }
    }
}
