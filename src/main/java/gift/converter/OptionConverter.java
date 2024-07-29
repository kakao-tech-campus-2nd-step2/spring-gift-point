package gift.converter;

import gift.dto.OptionDTO;
import gift.dto.OptionNameDTO;
import gift.dto.OptionQuantityDTO;
import gift.model.Option;
import gift.model.OptionName;
import gift.model.OptionQuantity;

public class OptionConverter {

    public static OptionDTO convertToDTO(Option option) {
        Long productId = null;
        if (option.getProduct() != null) {
            productId = option.getProduct().getId();
        }
        return new OptionDTO(option.getId(), new OptionNameDTO(option.getName().getName()), new OptionQuantityDTO(option.getQuantity().getQuantity()), productId);
    }

    public static Option convertToEntity(OptionDTO optionDTO) {
        return new Option(optionDTO.getId(), new OptionName(optionDTO.getName().getName()), new OptionQuantity(optionDTO.getQuantity().getQuantity()));
    }
}