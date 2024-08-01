package gift.service;

import gift.dto.OptionDto;
import gift.exception.CustomNotFoundException;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

  private final OptionRepository optionRepository;
  private final ProductRepository productRepository;

  @Autowired
  public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  public List<OptionDto> getOptionsByProductId(Long productId) {
    List<Option> options = optionRepository.findByProductId(productId);
    return options.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

  @Transactional
  public OptionDto addOptionToProduct(Long productId, OptionDto optionDto) {
    if (optionRepository.existsByNameAndProductId(optionDto.getName(), productId)) {
      throw new IllegalArgumentException("동일한 상품 내의 옵션 이름은 중복될 수 없습니다.");
    }

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomNotFoundException("Product not found"));

    Option option = new Option(optionDto.getName(), optionDto.getQuantity(), product);
    Option savedOption = optionRepository.save(option);
    return convertToDto(savedOption);
  }

  private OptionDto convertToDto(Option option) {
    return new OptionDto(option.getId(), option.getName(), option.getQuantity());
  }
}
