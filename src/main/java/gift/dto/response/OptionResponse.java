package gift.dto.response;

import gift.product.domain.Option;

public record OptionResponse(
	Long id,
	String name,
	Long quantity,
	Long productId
) {
	public static OptionResponse of(Long id, String name, Long quantity, Long productId) {
		return new OptionResponse(id, name, quantity,productId);
	}

	public static OptionResponse from(Option option) {
		return new OptionResponse(
			option.getId(),
			option.getName(),
			option.getQuantity(),
			option.getProduct().getId()
		);
	}
}
