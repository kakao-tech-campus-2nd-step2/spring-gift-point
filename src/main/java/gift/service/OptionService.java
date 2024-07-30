package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Option;
import gift.model.OptionDTO;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionDTO createOption(long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.PRODUCT_NOT_FOUND, productId));
        Option createdOption = new Option(optionDTO.name(), optionDTO.quantity(), product);
        return convertToDTO(optionRepository.save(createdOption));
    }

    public OptionDTO getOption(long optionId) {
        return convertToDTO(optionRepository.findById(optionId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.OPTION_NOT_FOUND, optionId)));
    }

    public OptionDTO updateOption(long productId, long optionId, OptionDTO optionDTO) {
        Option currentOption = optionRepository.findById(optionId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.OPTION_NOT_FOUND, optionId));
        currentOption.update(optionDTO.name(), optionDTO.quantity());
        return convertToDTO(optionRepository.save(currentOption));
    }

    public void deleteOption(long optionId) {
        optionRepository.findById(optionId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.OPTION_NOT_FOUND, optionId));
        optionRepository.deleteById(optionId);
    }

    private OptionDTO convertToDTO(Option option) {
        return new OptionDTO(option.getId(), option.getName(), option.getQuantity());
    }
}
