package gift.dto.response;

import gift.wishList.domain.WishProduct;

public record WishProductResponse(
	ProductResponse product
) {
	static public WishProductResponse from(WishProduct wishProduct) {
		return new WishProductResponse(ProductResponse.from(wishProduct.getProduct()));
	}
}
