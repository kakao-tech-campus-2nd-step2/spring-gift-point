package gift.domain.option;

import gift.domain.option.dto.OptionRequestDTO;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.option.OptionDuplicateException;
import gift.global.exception.option.OptionNotFoundException;
import gift.global.exception.product.ProductNotFoundException;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final JpaOptionRepository optionRepository;
    private final JpaProductRepository productRepository;

    public OptionService(JpaOptionRepository optionRepository,
        JpaProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getOptions() {
        List<Option> options = optionRepository.findAll();
        return options;
    }

    public List<Option> getOptionsByProductId(Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        return options;
    }

    // 기존 상품에 옵션 추가
    public void addOptionToExistsProduct(Long productId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Option> options = optionRepository.findAllByProductId(productId);
        validateDuplicateOptionName(optionRequestDTO.name(), options);

        Option option = new Option(optionRequestDTO.name(), optionRequestDTO.quantity(),
            product);

        optionRepository.save(option);
    }

    // 새 상품에 옵션 입력
    public void addOptionToNewProduct(Product savedProduct, OptionRequestDTO optionRequestDTO) {
        Option option = new Option(optionRequestDTO.name(), optionRequestDTO.quantity(),
            savedProduct);

        optionRepository.save(option);
    }

    // 옵션 수정
    @Transactional
    public void updateOption(Long productId, Long optionId, OptionRequestDTO optionRequestDTO) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Option> options = optionRepository.findAllByProductId(productId);

        for (Option option : options) {
            if (option.getName().equals(optionRequestDTO.name()) && option.getId() != optionId) {
                throw new OptionDuplicateException(optionRequestDTO.name());
            }
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException(optionId));

        option.update(optionRequestDTO.name(), optionRequestDTO.quantity());

        optionRepository.save(option);
    }

    // 상품 옵션 수량 차감
    @Transactional
    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        // 비관적 락 적용
        Option option = optionRepository.findByIdForUpdate(optionId)
            .orElseThrow(() -> new OptionNotFoundException(optionId));

        Product product = productRepository.findById(option.getProduct().getId())
            .orElseThrow(() -> new ProductNotFoundException(option.getProduct().getId()));

        if (option.getQuantity() < quantity || quantity <= 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "차감할 수량의 값이 올바르지 않습니다.");
        }

        if (Objects.equals(option.getQuantity(), quantity)) {
            // 상품에 옵션이 1개밖에 없을 때 - 옵션, 해당 옵션의 상품 모두 삭제
            if (product.hasOneOption()) {
                productRepository.delete(option.getProduct()); // Cascade 로 옵션도 삭제됨
                return;
            }
            // 상품에 옵션이 2개 이상일 때 - 옵션 삭제
            optionRepository.deleteById(optionId);
            return;
        }
        // 수량만 차감
        option.decrease(quantity);
    }

    private void validateDuplicateOptionName(String name, List<Option> options) {
        for (Option option : options) {
            if (option.getName().equals(name)) {
                throw new OptionDuplicateException(name);
            }
        }
    }

}
