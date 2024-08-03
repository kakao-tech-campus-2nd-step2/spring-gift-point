package gift.service;

import gift.dto.OptionRequest;
import gift.exception.ProductNotFoundException;
import gift.model.Option;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    @Autowired
    private final OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public void addOption(List<Option> options) {
        optionRepository.saveAll(options);
    }

    @Transactional
    public void updateOption(Long id, Option option) {
        if (!optionRepository.existsById(id)) {
            throw new ProductNotFoundException("Option not found");
        }

        if (optionRepository.existsById(id)) {
            Option option1 = optionRepository.findById(id).get();
            option1.setName(option.getName());
            option1.setQuantity(option.getQuantity());
            optionRepository.save(option1);
        }

    }
    @Transactional
    public void deleteOption(Long optionId) {
        if (!optionRepository.existsById(optionId)) {
            throw new ProductNotFoundException("Option not found");
        }
        Option option = optionRepository.findById(optionId).get();
        optionRepository.delete(option);
    }


    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public Optional<Option> getOptionById(Long id) {
        return optionRepository.findById(id);
    }

    public boolean existsOptionById(Long id) {
        return optionRepository.existsById(id);
    }

    public Option subtractOption(Option option, Long subtractQuantities) {
        if (subtractQuantities < 0) {
            throw new IllegalArgumentException("Subtract quantities는 양수여야 합니다!");
        }
        if (option.getQuantity() < subtractQuantities) {
            throw new IllegalArgumentException("감소시키기에 수량이 충분하지 않습니다! ");
        }

        option.setQuantity(option.getQuantity() - subtractQuantities);
        return optionRepository.save(option);
    }

    public List<Option> getOptionByProductID(Long productId){
        return optionRepository.findAllByProductId(productId);
    }


}
