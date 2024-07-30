package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.response.OptionResponse;
import gift.exception.DuplicateOptionNameException;
import gift.exception.MinimumOptionRequiredException;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.option.OptionSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static gift.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class OptionService {
    @Autowired
    private ProductSpringDataJpaRepository productRepository;

    @Autowired
    private OptionSpringDataJpaRepository optionRepository;

    public List<OptionResponse> getOptionsByProductId(Long productId) {
        if(!productRepository.existsById(productId)){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
                .map(option -> new OptionResponse(option.getId(), option.getName(), option.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Option addOptionToProduct(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        optionRepository.findByNameAndProductId(optionRequest.getName(), productId)
                .ifPresent(option -> {
                    throw new DuplicateOptionNameException(DUPLICATE_OPTION_NAME);
                });

        Option option = new Option(optionRequest);
        option.setProduct(product);

        return optionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        if(!productRepository.existsById(productId)){
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        Long count = optionRepository.countOptionByProductId(productId);

        if(count <= 1){
            throw new MinimumOptionRequiredException(MINIMUM_OPTION_REQUIRED);
        }

        Option option = optionRepository.findByIdAndProductId(optionId, productId)
                .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND));

        optionRepository.delete(option);
    }

    @Transactional
    public void subtractOptionQuantity(Long productId, Long optionId, int quantity) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }

        Option option = optionRepository.findByIdAndProductId(optionId, productId)
                .orElseThrow(() -> new OptionNotFoundException(OPTION_NOT_FOUND));

        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }

    public Long getProductIdByOptionId(Long optionId) {
        return optionRepository.findById(optionId)
                .map(Option::getProduct)
                .map(Product::getId)
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
    }

}
