package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.option.OptionDto;
import gift.exception.NoOptionsForProductException;
import gift.exception.NoSuchOptionException;
import gift.exception.NoSuchProductException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public OptionService(ProductRepository productRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    public List<OptionDto> getOptions(long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        return optionRepository.findByProduct(product)
            .stream()
            .map(option -> option.toDto())
            .toList();
    }

    public OptionDto addOption(long productId, OptionDto optionDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        return optionRepository.save(optionDto.toEntity(product)).toDto();
    }

    public OptionDto updateOption(long productId, OptionDto optionDto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        Option option = optionRepository.findByProductAndId(product, optionDto.optionId())
            .orElseThrow(NoSuchOptionException::new);
        option.update(optionDto.name(), optionDto.quantity());
        return optionRepository.save(option).toDto();
    }

    public OptionDto deleteOption(long productId, long id) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        if (product.getOptions().size() == 1) {
            throw new NoOptionsForProductException();
        }
        Option deletedOption = optionRepository.findById(id)
            .orElseThrow(NoSuchOptionException::new);
        optionRepository.delete(deletedOption);
        return deletedOption.toDto();
    }

    @Transactional
    public OptionDto buyOption(long id, int quantity) {
        Option option = optionRepository.findById(id)
            .orElseThrow(NoSuchOptionException::new);
        option.subtract(quantity);
        return optionRepository.save(option).toDto();
    }
}
