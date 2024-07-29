package gift.option;

import static gift.exception.ErrorMessage.OPTION_ALREADY_EXISTS;
import static gift.exception.ErrorMessage.OPTION_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;

import gift.option.entity.Option;
import gift.product.Product;
import gift.product.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(
        OptionRepository optionRepository,
        ProductRepository productRepository
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionDTO> getOptions(long productId) {
        validateProductExists(productId);

        return optionRepository.findAllByProductId(productId)
            .stream()
            .map(option -> new OptionDTO(
                option.getId(),
                option.getName(),
                option.getQuantity()
            )).toList();
    }

    public void addOption(long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        if (optionRepository.existsById(optionDTO.getId())) {
            throw new IllegalArgumentException(OPTION_ALREADY_EXISTS);
        }

        validateOptionExists(productId, optionDTO.getName());

        optionRepository.save(new Option(
            optionDTO.getId(),
            optionDTO.getName(),
            optionDTO.getQuantity(),
            product
        ));
    }

    public void updateOption(long productId, OptionDTO optionDTO) {
        validateProductExists(productId);
        validateOptionExists(productId, optionDTO.getName());

        Option option = getOptionById(optionDTO.getId());
        option.update(optionDTO.getName(), optionDTO.getQuantity());
        optionRepository.save(option);
    }

    public void deleteOption(long productId, long optionId) {
        validateProductExists(productId);

        Option option = getOptionById(optionId);
        optionRepository.delete(option);
    }

    public Option subtract(long optionId, int subtractOptionQuantity) {
        Option option = getOptionById(optionId);
        option.subtract(subtractOptionQuantity);
        return optionRepository.save(option);
    }

    private void validateProductExists(long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException(PRODUCT_NOT_FOUND);
        }
    }

    private void validateOptionExists(long productId, String optionName) {
        if (optionRepository.existsByNameAndProductId(optionName, productId)) {
            throw new IllegalArgumentException(OPTION_ALREADY_EXISTS);
        }
    }

    private Option getOptionById(long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException(OPTION_NOT_FOUND));
    }
}
