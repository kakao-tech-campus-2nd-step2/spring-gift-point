package gift.service;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionResponse addOption(Long productId, @Valid OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
        Option option = new Option(optionRequest.getName(), optionRequest.getQuantity(), product);
        Option savedOption = optionRepository.save(option);
        return OptionResponse.from(savedOption);
    }

    public OptionResponse updateOption(Long productId, Long optionId, @Valid OptionRequest optionRequest) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        option.updateOption(optionRequest.getName(), optionRequest.getQuantity());
        optionRepository.save(option);

        return OptionResponse.from(option);
    }

    public List<OptionResponse> getOptions(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
            .map(OptionResponse::from)
            .toList();
    }

    public void deleteOption(Long optionId, Long productId) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        List<Option> options = optionRepository.findByProductId(option.getProduct().getId());
        if (options.stream().count() == 1) {
            throw new IllegalArgumentException("상품 당 하나의 옵션은 있어야 합니다.");
        }

        optionRepository.delete(option);
    }

    public void decreaseOptionQuantity(Long optionId, Long productId, int quantity) {
        Option option = optionRepository.findByIdAndProductId(optionId, productId)
            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));

        if (option.getQuantity() < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }

}
