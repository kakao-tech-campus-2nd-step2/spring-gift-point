package gift.service;

import gift.dto.OptionDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Option> getOptionsByProductId(long productId) {
        return optionRepository.findByProductId(productId);
    }

    @Transactional
    public Option addOptionToProduct(Long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
        Option option = new Option();
        option.update(optionDTO.getName(), optionDTO.getQuantity());

        product.getOptions().add(option);
        return optionRepository.save(option);
    }

}
