package gift.service;

import gift.dto.OptionRequestDto;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.vo.Option;
import gift.vo.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다. "));
    }

    public Option getOption(Long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new NoSuchElementException("해당 옵션을 찾을 수 없습니다. "));
    }

    public List<Option> getOptionsPerProduct(Long id) {
        return optionRepository.findAllByProductId(id).orElseThrow(
                () -> new IllegalArgumentException("해당 상품의 옵션이 존재하지 않습니다."));
    }

    @Transactional
    public void addOption(List<OptionRequestDto> optionRequestDtos) {
        for (OptionRequestDto optionRequest : optionRequestDtos) {
            Product product = getProduct(optionRequest.productId());
            validateOptionNameDuplicate(optionRequest, product);
            Option option = optionRequest.toOption(product);
            product.addOption(option);
        }
    }

    @Transactional
    public void addOption(Product product, List<OptionRequestDto> optionRequestDtos) {
        for (OptionRequestDto optionRequest : optionRequestDtos) {
            validateOptionNameDuplicate(optionRequest, product);
            Option option = optionRequest.toOption(product);
            product.addOption(option);
        }
    }

    private void validateOptionNameDuplicate(OptionRequestDto optionRequestDto, Product product) {
        Optional<Option> existingOption = optionRepository.findByNameAndProductId(optionRequestDto.name(), product.getId());

        if (existingOption.isPresent()) {
            throw new IllegalArgumentException("상품 옵션명이 중복입니다. 다른 옵션명으로 변경해주세요.");
        }
    }

    public void subtractOptionQuantity(Long optionId, int quantity) {
        Option option = getOption(optionId);
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }
}
