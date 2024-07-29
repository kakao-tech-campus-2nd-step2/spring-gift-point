package gift.service;

import gift.domain.Option;
import gift.repository.OptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public List<Option> getProductOptions(Long product_id) {
        return optionRepository.getOptionsByProductId(product_id);
    }

    public void decreaseQuantity(Long option_id, Integer amount) {
        Option option = optionRepository.findById(option_id)
                .orElseThrow(() -> new EntityNotFoundException("옵션을 찾을 수 없습니다."));
        option.subtract(amount);
        optionRepository.save(option); // 수량 감소 후 업데이트
    }

    public Option getOption(Long optionId) {
        return optionRepository.findById(optionId).orElseThrow();
    }
}
