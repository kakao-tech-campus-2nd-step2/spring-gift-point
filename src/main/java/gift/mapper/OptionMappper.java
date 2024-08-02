package gift.mapper;

import gift.domain.option.Option;
import gift.domain.product.Product;
import gift.web.dto.option.OptionRequestDto;
import gift.web.dto.option.OptionResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OptionMappper {

    public OptionResponseDto toDto(Option option) {
        return new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }

    public Option toEntity(OptionRequestDto optionRequestDto, Product product) {
        return new Option(
            optionRequestDto.name(),
            optionRequestDto.quantity(),
            product
        );
    }
}
