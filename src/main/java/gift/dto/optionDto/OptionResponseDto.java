package gift.dto.optionDto;

public record OptionResponseDto(Long id,
                                Long productId,
                                String optionName,
                                int quantity) { }
