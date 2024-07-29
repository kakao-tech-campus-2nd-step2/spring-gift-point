package gift.service;

import gift.dto.OptionDTO;
import java.util.List;

public interface OptionService {
    List<OptionDTO> getOptionsByProductId(Long productId);
    OptionDTO getOptionById(Long id);
    void saveOption(OptionDTO optionDTO);
    void updateOption(Long id, OptionDTO optionDTO);
    void deleteOption(Long id);
}
