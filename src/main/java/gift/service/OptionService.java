package gift.service;

import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public OptionService(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponseDto> getOptions(Long productId) {
        Product product = getProduct(productId);
        return product.getOptions().stream().map(OptionResponseDto::new).toList();
    }

    @Transactional
    public void saveOption(OptionRequestDto request) {
        Option option = new Option(request.name(), request.quantity());
        optionRepository.save(option);
    }

    @Transactional
    public void subtractOption(Long id, Integer quantity) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_OPTION, id));
        option.subtract(quantity);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PRODUCT, productId));
    }
}
