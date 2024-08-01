package gift.service;

import gift.domain.OptionDTO;
import gift.entity.Option;
import gift.entity.Options;
import gift.repository.OptionRepository;
import gift.repository.OptionsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
public class OptionService {
    private final OptionsRepository optionsRepository;
    private final OptionRepository optionRepository;

    public OptionService (OptionsRepository optionsRepository, OptionRepository optionRepository) {
        this.optionsRepository = optionsRepository;
        this.optionRepository = optionRepository;
    }

    public Optional<Options> findByProduct_Id(int product_id) {
        return optionsRepository.findByProduct_Id(product_id);
    }

    public Option addOption(int product_id, OptionDTO optionDTO) {
        var savedOption = optionRepository.save(new Option(optionDTO));
        var options = findByProduct_Id(product_id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
        options.addOption(savedOption);
        optionsRepository.save(options);
        return savedOption;
    }

    public Option updateOption(int product_id, int id, OptionDTO optionDTO) {
        try {
            var savedOption = optionRepository.save(new Option(id, optionDTO));
            var options = findByProduct_Id(product_id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
            options.updateOption(savedOption);
            optionsRepository.save(options);
            return savedOption;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Option not found");
        }
    }

    public void deleteOption(int product_id, int id) {
        var options = findByProduct_Id(product_id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
        options.deleteOption(id);
        optionsRepository.save(options);
    }

    public int deductQuantity(int id, int quantity) {
        var currentQuantity = optionRepository.searchQuantityById(id);
        if (currentQuantity - quantity > 0) {
            optionRepository.updateQuantityById(id, currentQuantity - quantity);
        } else {
            throw new IllegalArgumentException("남은 수량이 없습니다.");
        }
        return currentQuantity - 1;
    }

    public int findProductIdByOptionId(int optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("Option not found with id: " + optionId));

        return optionsRepository.findProductIdByOptionListContaining(option)
                .orElseThrow(() -> new NoSuchElementException("Options not found for option id: " + optionId));
    }
}
