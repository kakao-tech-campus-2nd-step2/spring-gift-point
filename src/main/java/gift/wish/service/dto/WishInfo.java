package gift.wish.service.dto;

import gift.product.service.dto.ProductInfo;
import gift.wish.domain.Wish;

public record WishInfo(
        Long id,
        ProductInfo product
) {
    public static WishInfo from(Wish wish) {
        ProductInfo productInfo = ProductInfo.from(wish.getProduct());
        return new WishInfo(wish.getId(), productInfo);
    }
}
