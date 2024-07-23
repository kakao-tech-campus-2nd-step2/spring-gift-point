package gift.service;

import gift.dto.OptionRequest;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getOptionsByProductId(long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new CustomException.EntityNotFoundException("Product not found"));
        return product.getOptions();
    }

    public Option updateOption(Long optionId, String name, Long quantity) {
        Option option = optionRepository.findById(optionId).orElseThrow(()-> new CustomException.EntityNotFoundException("Option not found"));

        option.update(name, quantity);
        return optionRepository.save(option);
    }

    @Transactional
    public Option addOptionToProduct(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException.EntityNotFoundException("Product not found"));

        Optional<Option> existingOption = optionRepository.findByProductIdAndName(productId, optionRequest.getName());
        if (existingOption.isPresent()) {
            throw new CustomException.ValidationException("Duplicate option name");
        }

        Option option = new Option.Builder()
                .name(optionRequest.getName())
                .quantity(optionRequest.getQuantity())
                .product(product)
                .build();
        optionRepository.save(option);

        product.getOptions().add(option);
        return optionRepository.save(option);
    }

    public void deleteOption(Long optionId) {
        optionRepository.deleteById(optionId);
    }

    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        Option decreaseOption = optionRepository.findById(optionId).orElseThrow(() -> new CustomException.EntityNotFoundException("Option not found"));

        if (quantity <= 0) {
            throw new CustomException.InvalidQuantityException("Quantity must be greater than zero");
        }
        if(decreaseOption.getQuantity() < quantity) {
            throw new CustomException.InvalidQuantityException("Not enough quantity available");
        }
        Option updatedOption = new Option.Builder()
                .id(decreaseOption.getId())
                .name(decreaseOption.getName())
                .quantity(decreaseOption.getQuantity()-quantity)
                .product(decreaseOption.getProduct())
                .build();

        optionRepository.save(updatedOption);
    }
}
