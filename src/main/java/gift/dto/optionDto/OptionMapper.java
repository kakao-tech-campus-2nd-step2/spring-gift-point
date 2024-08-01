package gift.dto.optionDto;

import gift.model.product.Option;
import org.springframework.stereotype.Component;

@Component
public class OptionMapper {
    public OptionResponseDto tooOptionResponseDto(Option option){
        return new OptionResponseDto(option.getId(), option.getProduct().getId(), option.getName(), option.getQuantity());
    }
}
