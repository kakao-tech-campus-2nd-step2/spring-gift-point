package gift.service;

import gift.constants.Messages;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionQuantityRequestDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponse;
import gift.dto.OptionResponseDto;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addOption(Long id, OptionRequestDto optionRequestDto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(
                Messages.NOT_FOUND_PRODUCT_MESSAGE));
        Option option = new Option(optionRequestDto.getName(), optionRequestDto.getQuantity());
        option.setProduct(product);

        optionRepository.save(option);
    }

    public List<OptionResponseDto> findAll(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(Messages.NOT_FOUND_PRODUCT_MESSAGE));
        return product.getOptions()
            .stream()
            .map(option -> new OptionResponseDto(option.getId(), option.getName(),
                option.getQuantity()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteById(Long productId, Long optionId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(Messages.NOT_FOUND_PRODUCT_MESSAGE));
        validateNumberOfOptions(productId);
        product.deleteOption(optionId);
    }

    @Transactional
    public void updateOptionQuantity(Long productId, Long optionId,
        OptionQuantityRequestDto optionQuantityRequestDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(Messages.NOT_FOUND_PRODUCT_MESSAGE));
        Option option = product.getOptions().stream()
            .filter(o -> o.getId().equals(optionId))
            .findAny()
            .orElseThrow(() -> new OptionNotFoundException(Messages.NOT_FOUND_OPTION_MESSAGE));
        option.subtract(optionQuantityRequestDto.getQuantity());
        optionRepository.save(option);
    }

    private void validateNumberOfOptions(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(Messages.NOT_FOUND_PRODUCT_MESSAGE));
        if (product.getOptions().size() < 2) {
            throw new IllegalArgumentException(Messages.OPTION_BELOW_MINIMUM_MESSAGE);
        }
    }

    public void subtractQuantity(Long optionId, int quantity) {
        Option option = findById(optionId);
        int newQuantity = option.getQuantity() - quantity;
        if(newQuantity < 0){
            throw new IllegalArgumentException("수량이 부족합니다.");
        }
        option.setQuantity(newQuantity);
    }

    public Option findById(Long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException(Messages.NOT_FOUND_OPTION_MESSAGE));
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> findByProductId(Long productId){
        return optionRepository.findByProductId(productId)
            .stream()
            .map(OptionResponse::from)
            .toList();
    }
}
