package gift.dto.responsedto;

import gift.domain.Option;

public record OptionResponseDTO(Long id, String name, int quantity) {
    public static OptionResponseDTO from(Option option){
        return new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
