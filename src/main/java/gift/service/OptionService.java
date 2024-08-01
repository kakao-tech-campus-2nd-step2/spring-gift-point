package gift.service;

import gift.model.Product;
import gift.dto.OptionRequestDTO;
import gift.dto.OptionResponseDTO;
import gift.model.Option;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Tag(name = "옵션 관리 API", description = "옵션 관리를 위한 API")
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponseDTO> findALlByProductId(Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        return options.stream()
            .map(this::toDTO).toList();
    }

    public OptionResponseDTO findOptionById(Long optionId) {
        Option option = optionRepository.findById(optionId).orElse(null);
        return toDTO(option);
    }

    @Transactional
    public OptionResponseDTO saveOption(Long productId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElse(null);
        if (optionRepository.existsByProductIdAndName(productId, optionRequestDTO.name())) {
            throw new IllegalArgumentException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }
        Option option = new Option(null, optionRequestDTO.name(), optionRequestDTO.quantity(), product);
        optionRepository.save(option);
        return toDTO(option);
    }

    @Transactional
    public OptionResponseDTO updateOption(Long productId, Long optionId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElse(null);
        Option existingOption = optionRepository.findById(optionId).orElse(null);
        if (!existingOption.getName().equals(optionRequestDTO.name())
            && optionRepository.existsByProductIdAndName(productId, optionRequestDTO.name())) {
            throw new IllegalArgumentException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }
        existingOption.updateOption(optionRequestDTO.name(), optionRequestDTO.quantity());
        optionRepository.save(existingOption);
        return toDTO(existingOption);
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        if (options.size() <= 1) {
            throw new IllegalArgumentException("상품 정보에 항상 하나 이상의 옵션이 있어야 합니다.");
        }
        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractQuantity(Long optionId, Long subtractQuantity) {
        Option option = optionRepository.findById(optionId).orElse(null);
        option.subtractQuantity(subtractQuantity);
        optionRepository.save(option);
    }

    private OptionResponseDTO toDTO(Option option) {
        return new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity());
    }

    public Option toEntity(OptionResponseDTO optionResponseDTO) {
        return new Option(optionResponseDTO.id(), optionResponseDTO.name(), optionResponseDTO.quantity(), null);
    }

}