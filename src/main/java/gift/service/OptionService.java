package gift.service;

import gift.dto.optionDTO.OptionRequestDTO;
import gift.dto.optionDTO.OptionResponseDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponseDTO> findALlByProductId(Long productId) {
        List<Option> options = optionRepository.findAllByProductId(productId);
        if (options.isEmpty()) {
            throw new NotFoundException("옵션을 찾을 수 없습니다.");
        }
        return options.stream()
            .map(this::toDTO).toList();
    }

    public OptionResponseDTO findOptionById(Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException("옵션을 찾을 수 없습니다."));
        return toDTO(option);
    }

    @Transactional
    public OptionResponseDTO saveOption(Long productId, OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        if (optionRepository.existsByProductIdAndName(productId, optionRequestDTO.name())) {
            throw new InvalidInputValueException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
        }
        Option option = new Option(null, optionRequestDTO.name(), optionRequestDTO.quantity(),
            product);
        optionRepository.save(option);
        return toDTO(option);
    }

    @Transactional
    public OptionResponseDTO updateOption(Long productId, Long optionId,
        OptionRequestDTO optionRequestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
        Option existingOption = optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException("옵션을 찾을 수 없습니다."));
        if (!existingOption.getName().equals(optionRequestDTO.name())
            && optionRepository.existsByProductIdAndName(productId, optionRequestDTO.name())) {
            throw new InvalidInputValueException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
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
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException("옵션을 찾을 수 없습니다."));
        option.subtractQuantity(subtractQuantity);
        optionRepository.save(option);
    }

    private OptionResponseDTO toDTO(Option option) {
        return new OptionResponseDTO(option.getId(), option.getName(), option.getQuantity());
    }

    public Option toEntity(OptionResponseDTO optionResponseDTO) {
        return new Option(optionResponseDTO.id(), optionResponseDTO.name(),
            optionResponseDTO.quantity(), null);
    }

}
