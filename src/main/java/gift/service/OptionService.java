package gift.service;

import gift.domain.model.dto.OptionAddRequestDto;
import gift.domain.model.dto.OptionResponseDto;
import gift.domain.model.dto.OptionUpdateRequestDto;
import gift.domain.model.entity.Option;
import gift.domain.model.entity.Product;
import gift.domain.repository.OptionRepository;
import gift.domain.repository.ProductRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponseDto> getAllOptionsByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId).stream()
            .map(OptionResponseDto::toDto)
            .collect(Collectors.toList());
    }

    public OptionResponseDto addOption(Long productId, OptionAddRequestDto optionAddRequestDto) {
        validateOptionName(optionAddRequestDto.getName());

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("해당 상품이 존재하지 않습니다."));
        Option option = new Option(product, optionAddRequestDto.getName(),
            optionAddRequestDto.getQuantity());

        Option savedOption = optionRepository.save(option);
        return OptionResponseDto.toDto(savedOption);
    }

    public OptionResponseDto updateOption(Long id,
        OptionUpdateRequestDto optionUpdateRequestDto) {
        Option option = optionRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("해당 상품 옵션이 존재하지 않습니다."));

        option.update(id, optionUpdateRequestDto.getName(), optionUpdateRequestDto.getQuantity());
        Option savedOption = optionRepository.save(option);

        return OptionResponseDto.toDto(savedOption);
    }

    public void deleteOption(Long id) {
        validateOptionId(id);
        optionRepository.deleteById(id);
    }

    public OptionResponseDto subtractOptionQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("해당 상품 옵션이 존재하지 않습니다."));

        option.subtract(quantity);
        Option savedOption = optionRepository.save(option);

        return OptionResponseDto.toDto(savedOption);
    }

    private void validateOptionId(Long id) {
        if (!optionRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 상품 옵션이 존재하지 않습니다.");
        }
    }

    private void validateOptionName(String name) {
        if (optionRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 옵션입니다.");
        }
    }
}
