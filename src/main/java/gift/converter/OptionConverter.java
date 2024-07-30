package gift.converter;

import gift.dto.OptionDTO;
import gift.model.Option;

public class OptionConverter {

    public static OptionDTO convertToDTO(Option option) {
        Long productId = null;
        if (option.getProduct() != null) {
            productId = option.getProduct().getId();
        }
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity(), productId);
    }

    public static Option convertToEntity(OptionDTO optionDTO) {
        return new Option(optionDTO.getId(), optionDTO.getName(), optionDTO.getQuantity());
    }
}