package gift.dto.betweenClient.option;

import gift.entity.Option;

public record OptionResponseDTO(Long id, String name, Integer quantity) {
    public static OptionResponseDTO convertToDTO(Option option){
        return new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
