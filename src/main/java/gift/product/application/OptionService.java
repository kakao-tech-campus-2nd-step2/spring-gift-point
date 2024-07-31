package gift.product.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.product.dao.OptionRepository;
import gift.product.dao.ProductRepository;
import gift.product.dto.OptionRequest;
import gift.product.dto.OptionResponse;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.util.OptionMapper;
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
    public List<OptionResponse> getOptionsByProductId(Long id) {
        return optionRepository.findByProduct_Id(id)
                .stream()
                .map(OptionMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public OptionResponse addOptionToProduct(Long id, OptionRequest request) {
        Product product = productRepository.findProductAndOptionsById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Option option = OptionMapper.toEntity(request, product);
        if (!product.addOptionOrElseFalse(option)) {
            throw new CustomException(ErrorCode.OPTION_ALREADY_EXISTS);
        }

        return OptionMapper.toResponseDto(option);
    }

    @Transactional
    public void deleteOptionFromProductById(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.hasOnlyOneOption()) {
            throw new CustomException(ErrorCode.OPTION_REMOVE_FAILED);
        }
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        product.deleteOption(option);
    }

    @Transactional
    public void subtractQuantity(Option option,
                                 int quantity) {
        if (option.isLessEqual(quantity)) {
            throw new CustomException(ErrorCode.OPTION_QUANTITY_SUBTRACT_FAILED);
        }

        option.subtract(quantity);
    }

    public Option getOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));
    }

    @Transactional
    public OptionResponse updateOptionById(Long id, OptionRequest request) {
        Option option = getOptionById(id);
        option.update(request);

        return OptionMapper.toResponseDto(option);
    }

}
