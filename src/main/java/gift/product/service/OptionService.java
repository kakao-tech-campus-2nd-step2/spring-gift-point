package gift.product.service;

import gift.exception.DuplicateResourceException;
import gift.exception.ResourceNotFoundException;
import gift.product.dto.OptionRequestDto;
import gift.product.dto.OptionResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.*;
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

  public List<OptionResponseDto> getProductOptions(Long productId) {
    Product product = productRepository.findById(productId).orElseThrow(() ->
        new ResourceNotFoundException("해당 상품을 찾을 수 없습니다."));

    return product.getOptions().stream().map(option -> new OptionResponseDto(
        option.getId(), option.getQuantity(), option.getName()
    )).collect(Collectors.toList());
  }

  @Transactional
  public OptionResponseDto createOption(@Valid OptionRequestDto optionRequestDto, Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 상품을 찾을 수 없습니다."));

    if (optionRepository.existsByProductIdAndName(productId, optionRequestDto.optionName())) {
      throw new DuplicateResourceException("이미 존재하는 옵션 이름입니다.");
    }

    Option option = Option.builder().name(optionRequestDto.optionName()).quantity(
        optionRequestDto.optionQuantity()).product(product).build();

    product.addOption(option);
    productRepository.save(product);

    return new OptionResponseDto(
        option.getId(),
        option.getQuantity(),
        option.getName()
    );
  }

  @Transactional
  public OptionResponseDto updateOption(@Valid OptionRequestDto optionRequestDto, Long productId,
      Long optionId) {

    Option option = optionRepository.findByIdAndProductId(optionId, productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 옵션을 찾을 수 없습니다."));

    if (!option.getName().equals(optionRequestDto.optionName()) &&
        optionRepository.existsByProductIdAndName(productId, optionRequestDto.optionName())) {
      throw new DuplicateResourceException("이미 존재하는 옵션 이름입니다.");
    }

    option.update(optionRequestDto.optionName(), optionRequestDto.optionQuantity());

    Option updatedOption = optionRepository.save(option);

    return new OptionResponseDto(
        updatedOption.getId(),
        updatedOption.getQuantity(),
        updatedOption.getName()
    );
  }

  @Transactional
  public OptionResponseDto deleteOption(Long productId, Long optionId) {

    Option option = optionRepository.findByIdAndProductId(optionId, productId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "옵션을 찾을 수 없습니다."));

    OptionResponseDto deletedOptionDto = new OptionResponseDto(
        option.getId(),
        option.getQuantity(),
        option.getName()
    );

    optionRepository.delete(option);

    return deletedOptionDto;
  }



}
