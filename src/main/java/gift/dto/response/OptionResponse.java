package gift.dto.response;

import gift.product.domain.Option;

public record OptionResponse(
	Long id,
	String name,
	Long quantity
) {
	public static OptionResponse of(Long id, String name, Long quantity) {
		return new OptionResponse(id, name, quantity);
	}

	public static OptionResponse from(Option option) {
		return new OptionResponse(
			option.getId(),
			option.getName(),
			option.getQuantity()
		);
	}
}
