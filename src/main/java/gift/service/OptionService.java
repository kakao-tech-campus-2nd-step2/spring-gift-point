package gift.service;

import gift.common.dto.PageResponse;
import gift.common.exception.ErrorCode;
import gift.common.exception.OptionException;
import gift.common.exception.ProductException;
import gift.controller.option.dto.OptionRequest;
import gift.controller.option.dto.OptionResponse;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Long register(Long productId, OptionRequest.Create request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
        product.checkDuplicateName(request.name());

        Option option = request.toEntity();
        product.addOption(option);
        optionRepository.save(option);

        return option.getId();
    }

    public PageResponse<OptionResponse> getAllProductOptions(Long productId, Pageable pageable) {
        Page<Option> optionList = optionRepository.findAllByProductId(productId, pageable);
        List<OptionResponse> responses = optionList.getContent().stream().map(OptionResponse::from)
            .toList();
        return PageResponse.from(responses, optionList);
    }

    public OptionResponse findOption(Long id) {
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));
        return OptionResponse.from(option);
    }

    @Transactional
    public OptionResponse updateOption(Long productId, Long optionId,
        OptionRequest.Update request) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));
        Product product = option.getProduct();
        product.checkDuplicateName(request.name());
        option.updateOption(request.name(), request.quantity());
        return OptionResponse.from(option);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionException(ErrorCode.OPTION_NOT_FOUND));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.hasOneOption()) {
            throw new IllegalArgumentException("옵션이 1개 일때는 삭제할 수 없습니다.");
        }

        product.removeOption(option);
    }
}
