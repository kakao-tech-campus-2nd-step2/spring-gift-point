package gift.api.option.service;

import gift.api.option.domain.Option;
import gift.api.option.domain.Options;
import gift.api.option.dto.OptionRequest;
import gift.api.option.dto.OptionResponse;
import gift.api.option.repository.OptionRepository;
import gift.api.product.domain.Product;
import gift.api.product.repository.ProductRepository;
import gift.global.exception.NoSuchEntityException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponse> getOptions(Long productId) {
        return optionRepository.findAllByProductId(productId)
            .stream()
            .map(OptionResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void add(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchEntityException("product"));
        Option option = optionRequest.toEntity(product);
        Options.of(optionRepository.findAllByProductId(productId))
            .validateUniqueName(option);
        optionRepository.save(option);
    }

    @Transactional
    public void subtract(Long id, Integer quantity) {
        Option option = optionRepository.findByIdWithPessimisticWrite(id)
            .orElseThrow(() -> new NoSuchEntityException("option"));
        option.subtract(quantity);
    }

    public Option findOptionById(Long id) {
        return optionRepository.findById(id)
            .orElseThrow(() -> new NoSuchEntityException("option"));
    }
}
