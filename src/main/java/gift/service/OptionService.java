package gift.service;

import gift.dto.optionDto.OptionDto;
import gift.exception.ValueNotFoundException;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository,ProductRepository productRepository){
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getAllOptionsById(Long productId){
        return optionRepository.findByProductId(productId);
    }

    public void addNewOption(Long productId, OptionDto optionDto){
        Product product = productRepository.findById(productId).get();
        Option option = new Option(product, optionDto.name(), optionDto.amount());
        optionRepository.save(option);
    }

    @Transactional
    public void subtractAmount(Long optionId, int amount){
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ValueNotFoundException("Option not found with ID"));
        option.updateAmount(amount);
        optionRepository.save(option);
    }
}
