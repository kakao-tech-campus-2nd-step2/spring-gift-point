package gift.service;

import gift.common.exception.conflict.OptionNameConflictException;
import gift.common.exception.notFound.InvalidProductOptionException;
import gift.common.exception.notFound.ProductNotFoundException;
import gift.common.exception.notFound.OptionNotFoundException;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
            .orElseThrow(ProductNotFoundException::new);

        Optional<Option> existingOption = optionRepository.findByNameAndProductId(optionRequest.getName(), productId);
        if (existingOption.isPresent()) {
            throw new OptionNameConflictException();
        }

        Option option = new Option(optionRequest.getName(), optionRequest.getStockQuantity(), product);
        Option savedOption = optionRepository.save(option);
        return OptionResponse.from(savedOption);
    }

    public OptionResponse updateOption(Long productId, Long optionId, @Valid OptionRequest optionRequest) {
        if(!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(OptionNotFoundException::new);

        if(!option.getProduct().getId().equals(productId)) {
            throw new InvalidProductOptionException();
        }

        if (optionRepository.findByNameAndProductId(optionRequest.getName(), productId).isPresent()) {
            throw new OptionNameConflictException();
        }

        option.updateOption(optionRequest.getName(), optionRequest.getStockQuantity());
        optionRepository.save(option);

        return OptionResponse.from(option);
    }

    public List<OptionResponse> getOptions(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }

        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
            .map(OptionResponse::from)
            .toList();
    }

    public void deleteOption(Long productId, Long optionId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException();
        }

        Option option = optionRepository.findById(optionId)
            .orElseThrow(OptionNotFoundException::new);

        if (!option.getProduct().getId().equals(productId)) {
            throw new InvalidProductOptionException();
        }

        List<Option> options = optionRepository.findByProductId(option.getProduct().getId());
        if (options.stream().count() == 1) {
            throw new IllegalArgumentException("상품 당 하나의 옵션은 있어야 합니다.");
        }

        optionRepository.delete(option);
    }

}
