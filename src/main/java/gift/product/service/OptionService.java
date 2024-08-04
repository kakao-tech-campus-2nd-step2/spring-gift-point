package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.CANNOT_SUBTRACT_ZERO_OR_NEGATIVE;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.SUBTRACT_EXCEED_QUANTITY;

import gift.product.dto.OptionDTO;
import gift.product.dto.OrderRequestDTO;
import gift.product.exception.InstanceValueException;
import gift.product.exception.InvalidIdException;
import gift.product.model.Option;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.validation.OptionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    private final OptionValidation optionValidation;

    @Autowired
    public OptionService(
        OptionRepository optionRepository,
        ProductRepository productRepository,
        OptionValidation optionValidation
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
        this.optionValidation = optionValidation;
    }

    public Page<Option> getAllOptions(Long id, Pageable pageable) {
        System.out.println("[OptionService] getAllOptions()");
        return optionRepository.findAllByProductId(id, pageable);
    }

    public Option registerOption(Long productId, OptionDTO optionDTO) {
        System.out.println("[OptionService] registerOption()");
        Option option = optionDTO.convertToDomain(productId, productRepository);
        optionValidation.register(option);
        return optionRepository.save(option);
    }

    public Option updateOption(Long id, Long productId, OptionDTO optionDTO) {
        System.out.println("[OptionService] updateOption()");
        Option option = optionDTO.convertToDomain(id, productId, productRepository);
        optionValidation.update(option);
        return optionRepository.save(option);
    }

    public void deleteOption(Long id, Long productId) {
        System.out.println("[OptionService] deleteOption()");
        optionValidation.delete(id, productId);
        optionRepository.deleteById(id);
    }

    public Option findById(Long id) {
        System.out.println("[OptionService] findById()");
        return optionRepository.findById(id)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }

    public Option subtractQuantity(OrderRequestDTO orderRequestDTO) {
        if(orderRequestDTO.getQuantity() < 1)
            throw new InstanceValueException(CANNOT_SUBTRACT_ZERO_OR_NEGATIVE);
        Option option = optionRepository.findById(orderRequestDTO.getOptionId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        if(option.getQuantity() < orderRequestDTO.getQuantity())
            throw new InstanceValueException(SUBTRACT_EXCEED_QUANTITY);
        option.subtractQuantity(orderRequestDTO.getQuantity());
        return optionRepository.save(option);
    }
}
