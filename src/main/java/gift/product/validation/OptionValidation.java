package gift.product.validation;

import static gift.product.exception.GlobalExceptionHandler.DUPLICATE_OPTION_NAME;
import static gift.product.exception.GlobalExceptionHandler.LAST_OPTION;
import static gift.product.exception.GlobalExceptionHandler.LEAST_QUANTITY;
import static gift.product.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static gift.product.exception.GlobalExceptionHandler.OVER_100MILLION;

import gift.product.exception.DuplicateException;
import gift.product.exception.InstanceValueException;
import gift.product.exception.InvalidIdException;
import gift.product.exception.LastOptionException;
import gift.product.model.Option;
import gift.product.repository.OptionRepository;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionValidation {

    private final OptionRepository optionRepository;

    @Autowired
    public OptionValidation(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public void register(Option option) {
        List<Option> options = optionRepository.findAllByProduct(option.getProduct());
        validateOver100Million(options.size());
        validateDuplicateName(options, option);
        validateNegative(option.getQuantity());
    }

    public void update(Option option) {
        validateExistId(option.getId());
        List<Option> options = optionRepository.findAllByProduct(option.getProduct());
        validateDuplicateName(options, option);
        validateNegative(option.getQuantity());
    }

    public void delete(Long id, Long productId) {
        validateExistId(id);
        validateLastOption(optionRepository.countByProduct(productId));
    }

    private void validateExistId(Long id) {
        if(!optionRepository.existsById(id))
            throw new InvalidIdException(NOT_EXIST_ID);
    }

    private void validateOver100Million(int size) {
        if(size >= 99_999_999)
            throw new InstanceValueException(OVER_100MILLION);
    }

    private void validateDuplicateName(Collection<Option> options, Option newOption) {
        if(options.stream()
            .anyMatch(newOption::isSameName))
            throw new DuplicateException(DUPLICATE_OPTION_NAME);
    }

    private void validateNegative(int quantity) {
        if(quantity < 0)
            throw new InstanceValueException(LEAST_QUANTITY);
    }

    private void validateLastOption(int cnt) {
        if(cnt < 2)
            throw new LastOptionException(LAST_OPTION);
    }
}
