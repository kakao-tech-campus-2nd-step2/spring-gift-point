package gift.dto.request;

import gift.core.annotaions.ValidCharset;
import gift.core.exception.ValidationMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record OptionRequest(
	@ValidCharset
	String name,
	@Min(value = 1, message = ValidationMessage.INVALID_SIZE_MSG)
	@Max(value = 100_000_000, message = ValidationMessage.INVALID_SIZE_MSG)
	Long quantity
) {
	public static OptionRequest of(String name, Long quantity) {
		return new OptionRequest(name, quantity);
	}
}
