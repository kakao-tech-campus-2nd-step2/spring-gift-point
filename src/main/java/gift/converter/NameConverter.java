package gift.converter;

import gift.dto.NameDTO;
import gift.model.Name;

public class NameConverter {

    public static NameDTO convertToDTO(Name name) {
        return new NameDTO(name.getName());
    }

    public static Name convertToEntity(NameDTO nameDTO) {
        return new Name(nameDTO.getName());
    }
}