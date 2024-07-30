package gift.service;

import gift.dto.OptionDTO;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> findAllByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId);
    }

    public Option findOptionById(Long optionId) {
        return optionRepository.findById(optionId).orElse(null);
    }

    @Transactional
    public void saveOption(OptionDTO optionDTO) {
        Product product = productRepository.findById(optionDTO.productId()).orElse(null);
        if (optionRepository.existsByProductIdAndName(optionDTO.productId(), optionDTO.name())) {
            throw new IllegalArgumentException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }
        Option option = new Option(null, optionDTO.name(), optionDTO.quantity(), product);
        optionRepository.save(option);
    }

    @Transactional
    public void updateOption(OptionDTO optionDTO, Long optionId) {
        Option existingOption = optionRepository.findById(optionId).orElse(null);
        if (!existingOption.getName().equals(optionDTO.name())
            && optionRepository.existsByProductIdAndName(optionDTO.productId(), optionDTO.name())) {
            throw new IllegalArgumentException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }
        existingOption.updateOption(optionDTO.name(), optionDTO.quantity());
        optionRepository.save(existingOption);
    }

    @Transactional
    public void deleteOption(Long optionId, Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        if (options.size() <= 1) {
            throw new IllegalArgumentException("상품 정보에 항상 하나 이상의 옵션이 있어야 합니다.");
        }
        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractQuantity(Long optionId, Long subtractQuantity) {
        Option option = optionRepository.findById(optionId).orElse(null);
        option.subtractQuantity(subtractQuantity);
        optionRepository.save(option);
    }

    public static OptionDTO toDTO(Option option) {
        return new OptionDTO(option.getName(), option.getQuantity(), option.getProduct().getId());
    }

}
