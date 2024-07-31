package gift.product.option.service;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.product.entity.Product;
import gift.product.option.dto.request.CreateOptionRequest;
import gift.product.option.dto.request.UpdateOptionRequest;
import gift.product.option.dto.response.OptionResponse;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionJpaRepository;
import gift.product.repository.ProductJpaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final ProductJpaRepository productRepository;
    private final OptionJpaRepository optionRepository;

    public OptionService(ProductJpaRepository productRepository,
        OptionJpaRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> getProductOptions(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return product.getOptions()
            .getSet()
            .stream()
            .map(OptionResponse::from)
            .toList();
    }

    @Transactional
    public OptionResponse createOption(Long productId, CreateOptionRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        Option option = new Option(request.name(), request.quantity(), product);

        product.addOption(option);
        Option saved = optionRepository.save(option);

        return OptionResponse.from(saved);
    }

    @Transactional
    public void updateOption(Long productId, Long id, UpdateOptionRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.editOption(id, request.name(), request.quantity());
    }

    @Transactional
    public void deleteOption(Long productId, Long id) {
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.removeOption(option);
        optionRepository.delete(option);
    }

    @Transactional
    public Option subtractOptionQuantity(Long id, Integer subtractionQuantity) {
        Option option = optionRepository.findByIdWithPessimisticLocking(id)
            .orElseThrow(() -> new CustomException(ErrorCode.OPTION_NOT_FOUND));

        option.subtract(subtractionQuantity);
        return option;
    }
}
