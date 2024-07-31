package gift.dto.request;

import java.util.List;

import gift.core.annotaions.NoKakao;
import gift.core.annotaions.ValidCharset;
import gift.core.exception.ValidationMessage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
	@Size(max = 15, message = ValidationMessage.INVALID_SIZE_MSG)
	@ValidCharset
	@NoKakao
	String name,
	Long price,
	String imageUrl,
	@NotNull(message = ValidationMessage.NOT_NULL_MSG)
	String categoryName,
	@Valid
	List<OptionRequest> options
) {
}
