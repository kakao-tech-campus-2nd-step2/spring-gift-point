package gift.service;

import gift.dto.OptionDTO;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OptionDTO addOptionToProduct(Long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + productId));

        if (optionRepository.findByProductId(productId).stream()
                .anyMatch(option -> option.getName().equals(optionDTO.getName()))) {
            throw new IllegalArgumentException("Option name already exists for this product.");
        }

        Option option = new Option(null, optionDTO.getName(), optionDTO.getQuantity(), product);
        optionRepository.save(option);
        return new OptionDTO(option);
    }

    @Transactional
    public OptionDTO updateOption(Long productId, Long optionId, OptionDTO optionDTO) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option Id:" + optionId));
        if (!option.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the given product.");
        }
        option.setName(optionDTO.getName());
        option.setQuantity(optionDTO.getQuantity());
        optionRepository.save(option);
        return new OptionDTO(option);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option Id:" + optionId));
        if (!option.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the given product.");
        }
        optionRepository.delete(option);
    }

    public List<OptionDTO> getOptionsByProductId(Long productId) {
        return optionRepository.findByProductId(productId).stream().map(OptionDTO::new).toList();
    }
}