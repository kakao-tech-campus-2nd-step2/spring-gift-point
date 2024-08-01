package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductService productService;

    public OptionService(OptionRepository optionRepository, ProductService productService) {
        this.optionRepository = optionRepository;
        this.productService = productService;
    }

    public void subtractOptionQuantity(Long productId, Long optionId, int quantityToSubtract) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid option Id:" + optionId));

        if (!option.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Option does not belong to the given product");
        }

        if (option.getQuantity() < quantityToSubtract) {
            throw new IllegalArgumentException("남아있는 수량이 더 작습니다.");
        }

        option.subtract(quantityToSubtract);
        optionRepository.save(option);
    }

    public List<OptionResponseDTO> getOptionsByProductId(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream()
                .map(option -> new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity()))
                .collect(Collectors.toList());
    }

    public void addOption(Long productId, OptionRequestDTO optionRequest) {
        Product product = productService.getProductEntityById(productId);
        Option option = new Option(optionRequest.name(), optionRequest.quantity(), product);
        optionRepository.save(option);
    }

    public void deleteOption(Long optionId) {
        optionRepository.deleteById(optionId);
    }
}