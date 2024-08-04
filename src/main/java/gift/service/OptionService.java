package gift.service;

import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.OptionRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    private boolean isDuplicateName(Option option) {
        Optional<Option> options = optionRepository.findByIdAndName(
            option.getProduct().getId(), option.getName()
        );
        return options.isPresent();
    }

    public void addOption(Option option) {
        if (isDuplicateName(option)) {
            throw new CustomException(ErrorCode.OPTION_NAME_DUPLICATED);
        }
        optionRepository.save(option);
    }

    public void updateOption(Option option, Long optionId) {
        Option update = getOptionById(optionId);
        update.setName(option.getName());
        update.setQuantity(option.getQuantity());
        optionRepository.save(update);
    }

    public void deleteOption(Long id) {
        getOptionById(id);
        optionRepository.deleteById(id);
    }

    public Option getOptionById(Long id) {
        Optional<Option> option = optionRepository.findById(id);

        if (option.isEmpty()) {
            throw new CustomException(ErrorCode.OPTION_NOT_FOUND);
        }
        return option.get();
    }

    public List<Option> getOptionByProductId(Long productId) {
        return optionRepository.getOptionByProductId(productId);
    }

    public void subtractOption(Long id) {
        Option option = getOptionById(id);
        int quantity = option.getQuantity();
        if (quantity < 1) {
            throw new CustomException(ErrorCode.OPTION_NOT_SUBSTRACT);
        }
        option.setQuantity(quantity - 1);
        optionRepository.saveAndFlush(option);
    }

    public void subtractOption(Long id, int quantity) {
        Option option = getOptionById(id);
        int optionQuantity = option.getQuantity();
        if (optionQuantity < quantity) {
            throw new CustomException(ErrorCode.OPTION_NOT_SUBSTRACT);
        }
        option.setQuantity(optionQuantity - quantity);
        optionRepository.saveAndFlush(option);
    }

}
