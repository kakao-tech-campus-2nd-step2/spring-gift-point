package gift.product.service;

import gift.exception.BadRequestException;
import gift.exception.ResourceNotFoundException;
import gift.product.dto.OptionDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  public OptionDto createOption(@Valid OptionDto optionDto, Long productId) {
    validateOption(optionDto);
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상품을 찾을 수 없습니다: " + productId));
    Option option = OptionDto.toEntity(optionDto, product);
    Option savedOption = optionRepository.save(option);
    return OptionDto.toDto(savedOption);
  }

  @Transactional
  public OptionDto updateOption(@Valid OptionDto optionDto) {
    validateOption(optionDto);
    Option option = optionRepository.findById(optionDto.getId())
        .orElseThrow(
            () -> new ResourceNotFoundException("해당 ID의 옵션을 찾을 수 없습니다: " + optionDto.getId()));
    option.setName(optionDto.getName());
    option.setQuantity(optionDto.getQuantity());
    Option updatedOption = optionRepository.save(option);
    return OptionDto.toDto(updatedOption);
  }

  @Transactional(readOnly = true)
  public OptionDto getOptionById(Long id) {
    Option option = optionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 옵션을 찾을 수 없습니다: " + id));
    return OptionDto.toDto(option);
  }

  @Transactional
  public void deleteOption(Long id) {
    if (!optionRepository.existsById(id)) {
      throw new ResourceNotFoundException("해당 ID의 옵션을 찾을 수 없습니다: " + id);
    }
    optionRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public boolean optionExistsByProductIdAndName(Long productId, String name) {
    return optionRepository.existsByProductIdAndName(productId, name);
  }

  @Transactional(readOnly = true)
  public List<OptionDto> getAllOptionsByProductId(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 ID의 상품을 찾을 수 없습니다: " + productId));

    List<Option> options = optionRepository.findAllByProduct(product);
    return options.stream()
        .map(OptionDto::toDto)
        .collect(Collectors.toList());
  }

  private void validateOption(OptionDto optionDto) {
    if (optionDto.getName() == null || optionDto.getName().trim().isEmpty()) {
      throw new BadRequestException("옵션 이름은 비어 있을 수 없습니다.");
    }
    if (!optionDto.getName().matches("^[\\w\\s\\(\\)\\[\\]\\+\\-&/]{1,50}$")) {
      throw new BadRequestException("옵션 이름 형식이 올바르지 않습니다: " + optionDto.getName());
    }
    if (optionDto.getQuantity() < 1 || optionDto.getQuantity() >= 100000000) {
      throw new BadRequestException("옵션 수량은 1 이상 1억 미만이어야 합니다: " + optionDto.getQuantity());
    }
  }
}
