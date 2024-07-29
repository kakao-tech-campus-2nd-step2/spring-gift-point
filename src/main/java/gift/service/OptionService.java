package gift.service;

import gift.entity.Option;
import gift.entity.Product;
import gift.exception.DataNotFoundException;
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
            throw new IllegalArgumentException("중복Option이름 사용 불가능");
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
            throw new DataNotFoundException("존재하지 않는 Option");
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
            throw new IllegalStateException("상품 수량 부족");
        }
        option.setQuantity(quantity - 1);
        optionRepository.saveAndFlush(option);
    }

    public void subtractOption(Long id, int quantity) {
        Option option = getOptionById(id);
        int optionQuantity = option.getQuantity();
        if (optionQuantity < quantity) {
            throw new IllegalStateException("상품 수량 부족");
        }
        option.setQuantity(optionQuantity - quantity);
        optionRepository.saveAndFlush(option);
    }

}
