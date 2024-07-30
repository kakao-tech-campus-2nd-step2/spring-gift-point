package gift.dto.requestdto;

import jakarta.validation.Valid;

public record ProductCreateRequestDTO(
    @Valid ProductRequestDTO productRequestDTO,
    @Valid OptionCreateRequestDTO optionCreateRequestDTO
) {
}
