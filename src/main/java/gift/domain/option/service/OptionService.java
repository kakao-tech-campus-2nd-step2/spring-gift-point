package gift.domain.option.service;

import gift.domain.option.dto.OptionRequest;
import gift.domain.option.dto.OptionResponse;
import gift.domain.option.entity.Option;
import gift.domain.option.exception.OptionNotFoundException;
import gift.domain.option.repository.OptionRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.exception.ProductNotFoundException;
import gift.domain.product.repository.ProductRepository;
import java.util.List;
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

    public List<OptionResponse> getProductOptions(Long productId) {
        Product savedProduct = productRepository.findById(productId)
            .orElseThrow(() -> new OptionNotFoundException("[상품 옵션 조회] 찾는 상품이 없습니다."));
        List<Option> savedOptions = optionRepository.findAllByProduct(savedProduct);
        return savedOptions.stream().map(this::entityToDto).toList();
    }

    public Option getOption(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new OptionNotFoundException("찾는 옵션이 존재하지 않습니다."));
    }

    @Transactional
    public void addOptionToProduct(Long id, OptionRequest request) {

        Product savedProduct = productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));

        List<Option> savedOptionList = optionRepository.findAllByProduct(savedProduct);

        Option newOption = dtoToEntity(request);
        newOption.checkDuplicateName(savedOptionList);
        newOption.addProduct(savedProduct);
        optionRepository.save(newOption);
    }

    @Transactional
    public void subtractQuantity(Long id, int quantity) {
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionNotFoundException("해당 옵션이 존재하지 않습니다."));
        option.subtractQuantity(quantity);
    }

    @Transactional
    public void updateProductOption(Long productId, Long optionId, OptionRequest request) {
        Option savedOption = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException("해당 옵션이 존재하지 않습니다."));
        if (!savedOption.getProduct().getId().equals(productId)) {
            throw new OptionNotFoundException("해당 상품에 대한 옵션이 아닙니다.");
        }
        savedOption.updateNameAndQuantity(request.getName(), request.getQuantity());
    }

    @Transactional
    public void deleteProductOption(Long productId, Long optionId) {

        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new OptionNotFoundException("해당 옵션이 존재하지 않습니다."));
        if (!option.getProduct().getId().equals(productId)) {
            throw new OptionNotFoundException("해당 상품에 존재하지 않는 옵션입니다.");
        }
        optionRepository.delete(option);
    }

    private OptionResponse entityToDto(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity());
    }

    private Option dtoToEntity(OptionRequest request) {
        return new Option(request.getName(), request.getQuantity());
    }
}
