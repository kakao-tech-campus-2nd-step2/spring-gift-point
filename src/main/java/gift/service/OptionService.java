package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionDTO;
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

    public List<OptionDTO> getOptions(long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        return optionRepository.findByProduct(product)
            .stream()
            .map(option -> option.toDTO())
            .toList();
    }

    public OptionDTO addOption(long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        return optionRepository.save(optionDTO.toEntity(product)).toDTO();
    }

    public OptionDTO updateOption(long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        optionRepository.findByProductAndId(product, optionDTO.id())
            .orElseThrow(NoSuchOptionException::new);
        Option updatedOption = new Option(optionDTO.id(), optionDTO.name(), optionDTO.quantity(), product);
        return optionRepository.save(updatedOption).toDTO();
    }

    public OptionDTO deleteOption(long productId, long id) {
        Product product = productRepository.findById(productId)
            .orElseThrow(NoSuchProductException::new);
        if (product.getOptions().size() == 1) {
            throw new NoOptionsForProductException();
        }
        Option deletedOption = optionRepository.findById(id)
            .orElseThrow(NoSuchOptionException::new);
        optionRepository.delete(deletedOption);
        return deletedOption.toDTO();
    }

    @Transactional
    public OptionDTO buyOption(long id, int quantity) {
        Option option = optionRepository.findById(id)
            .orElseThrow(NoSuchOptionException::new);
        option.subtract(quantity);
        return optionRepository.save(option).toDTO();
    }
}
