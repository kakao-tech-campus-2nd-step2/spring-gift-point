package gift.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import gift.product.domain.Product;

public record ProductResponse(
	Long id,
	String name,
	Long price,
	String imageUrl
	// String categoryName,
	// List<OptionResponse> options
) {
	static public ProductResponse from(Product product) {
		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getPrice(),
			product.getImageUrl()
			// product.getCategory().getName(),
			// product.getOptions().stream()
			// 	.map(OptionResponse::from)
			// 	.collect(Collectors.toList())
		);
	}
}
