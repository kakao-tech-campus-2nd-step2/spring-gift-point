package gift.domain.option;

import gift.domain.option.dto.request.OptionRequest;
import gift.domain.option.dto.response.OptionResponse;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.option.OptionDuplicateException;
import gift.global.exception.option.OptionNotFoundException;
import gift.global.exception.option.OptionOnlyOneDeleteException;
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

    public List<OptionResponse> getOptions(Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        List<OptionResponse> optionsResponse = options.stream()
            .map(option -> option.toOptionResponse()).toList();

        return optionsResponse;
    }

    // 기존 상품에 옵션 추가
    public void addOptionToExistsProduct(Long productId, gift.domain.option.dto.request.OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Option> options = optionRepository.findAllByProductId(productId);
        validateDuplicateOptionName(optionRequest.name(), options);

        Option option = new Option(optionRequest.name(), optionRequest.quantity(),
            product);

        optionRepository.save(option);
    }

    // 새 상품에 옵션들 입력
    public void addOptionsToNewProduct(Product savedProduct, List<OptionRequest> optionRequests) {
        for (OptionRequest optionRequest : optionRequests) {
            Option option = new Option(optionRequest.name(), optionRequest.quantity(),
                savedProduct);

            optionRepository.save(option);
        }
    }

    // 옵션 수정
    @Transactional
    public void updateOption(Long productId, Long optionId, OptionRequest optionRequest) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Option> options = optionRepository.findAllByProductId(productId);

        for (Option option : options) {
            if (option.getName().equals(optionRequest.name()) && option.getId() != optionId) {
                throw new OptionDuplicateException(optionRequest.name());
            }
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException(optionId));

        option.update(optionRequest.name(), optionRequest.quantity());
    }

    // 옵션 삭제
    public void deleteOption(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(productId));
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException(optionId));
        // 상품에 옵션이 한개인 경우
        if (product.getOptions().size() == 1) {
            throw new OptionOnlyOneDeleteException(optionId);
        }

        optionRepository.deleteById(optionId);
    }

    // 상품 옵션 수량 차감
    @Transactional
    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        // 비관적 락 적용
        Option option = optionRepository.findByIdForUpdate(optionId)
            .orElseThrow(() -> new OptionNotFoundException(optionId));

        Product product = productRepository.findById(option.getProduct().getId())
            .orElseThrow(() -> new ProductNotFoundException(option.getProduct().getId()));

        if (quantity <= 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "차감할 수량의 값은 1 이상이어야 합니다.");
        }
        if (option.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "차감할 수량이 옵션의 잔여 수량보다 많습니다.");
        }

        // 수량 차감 - 차감 수량이 옵션의 잔여 수량과 같은 경우 잔여 수량이 0이 됨
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