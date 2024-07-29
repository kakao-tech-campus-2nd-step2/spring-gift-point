package gift.service;

import gift.dto.OptionQuantityRequest;
import gift.dto.OptionRequest;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
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
    public Option addOption(Long productId, OptionRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product ID에 해당하는 Product가 없습니다."));

        return optionRepository.save(
            new Option(request.getName(), request.getQuantity(), product));
    }

    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public List<Option> getAllOptionsByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId);
    }

    public Option getOneOptionById(Long id) {
        return optionRepository.findById(id).
            orElseThrow(() -> new OptionNotFoundException("ID에 해당하는 옵션이 없습니다."));
    }

    @Transactional
    public Option updateOption(Long productId, Long id, OptionRequest request) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product ID에 해당하는 Product가 없습니다."));
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionNotFoundException("ID에 해당하는 옵션이 없습니다."));

        option.updateOption(request.getName(), request.getQuantity(), product);

        return option;
    }

    public void deleteOption(Long productId, Long id) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product ID에 해당하는 Product가 없습니다."));
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionNotFoundException("ID에 해당하는 옵션이 없습니다."));

        product.removeOption(option, product);
        productRepository.save(product);
        optionRepository.deleteById(id);
    }

    @Transactional
    public Option subtractOptionQuantity(Long productId, Long id, OptionQuantityRequest request) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("Product id에 해당하는 상품이 없습니다.");
        }
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new OptionNotFoundException("Option id에 해당하는 옵션이 없습니다."));
        option.subtractQuantity(request.quantity());
        return option;
    }

}
