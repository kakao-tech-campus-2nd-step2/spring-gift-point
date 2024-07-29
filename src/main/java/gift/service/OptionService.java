package gift.service;

import gift.dto.OptionRequest;
import gift.dto.OrderRequest;
import gift.exception.AlreadyExistsException;
import gift.exception.NotFoundException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getOptionByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 id의 상품이 존재하지 않습니다."));
        return product.getOptions();
    }

    @Transactional
    public Option makeOption(Long productId, OptionRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 id의 상품이 존재하지 않습니다."));
        if (product.checkDuplicateOptionName(request.name())) {
            throw new AlreadyExistsException("해당 상품에 등록된 옵션과 동일한 이름이 존재합니다.");
        }
        Option option = new Option(
                request.name(),
                request.quantity(),
                product
        );
        return optionRepository.save(option);
    }

    @Transactional
    public void subtractQuantity(OrderRequest request) {
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new NotFoundException("해당 id의 옵션이 존재하지 않습니다."));
        option.subtract(request.quantity());
    }

    public Product getProductById(OrderRequest request) {
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new NotFoundException("해당 id의 옵션이 존재하지 않습니다."));
        return option.getProduct();
    }
}
