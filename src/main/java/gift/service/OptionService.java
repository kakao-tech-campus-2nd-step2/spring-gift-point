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

        // 동일한 상품 내에서 옵션 이름 중복 체크
        if (optionRepository.findByProductId(productId).stream()
                .anyMatch(option -> option.getName().equals(optionDTO.getName()))) {
            throw new IllegalArgumentException("Option name already exists for this product.");
        }

        Option option = new Option(null, optionDTO.getName(), optionDTO.getQuantity(), product);
        optionRepository.save(option);
        return new OptionDTO(option);
    }

    public List<OptionDTO> getOptionsByProductId(Long productId) {
        return optionRepository.findByProductId(productId).stream().map(OptionDTO::new).toList();
    }
}