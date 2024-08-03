package gift.service;

import gift.dto.ProductOptionDto;
import gift.exception.CustomNotFoundException;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOptionService {

  private final ProductOptionRepository productOptionRepository;
  private final ProductRepository productRepository;

  @Autowired
  public ProductOptionService(ProductOptionRepository productOptionRepository, ProductRepository productRepository) {
    this.productOptionRepository = productOptionRepository;
    this.productRepository = productRepository;
  }

  public ProductOptionDto addOption(Long productId, ProductOptionDto optionDto) {
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomNotFoundException("Product not found"));
    ProductOption option = new ProductOption(optionDto.getName(), optionDto.getValue(), product);
    ProductOption savedOption = productOptionRepository.save(option);
    return convertToDto(savedOption);
  }

  public ProductOptionDto updateOption(Long productId, Long optionId, ProductOptionDto optionDto) {
    ProductOption existingOption = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new CustomNotFoundException("Option not found"));
    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomNotFoundException("Product not found"));

    ProductOption updatedOption = new ProductOption(
            optionId,
            optionDto.getName() != null ? optionDto.getName() : existingOption.getName(),
            optionDto.getValue() != null ? optionDto.getValue() : existingOption.getValue(),
            product
    );

    ProductOption savedOption = productOptionRepository.save(updatedOption);
    return convertToDto(savedOption);
  }

  public void deleteOption(Long productId, Long optionId) {
    ProductOption option = productOptionRepository.findById(optionId)
            .orElseThrow(() -> new CustomNotFoundException("Option not found"));
    productOptionRepository.delete(option);
  }

  public List<ProductOptionDto> getOptionsByProductId(Long productId) {
    List<ProductOption> options = productOptionRepository.findByProductId(productId);
    return options.stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private ProductOptionDto convertToDto(ProductOption option) {
    return new ProductOptionDto(option.getId(), option.getName(), option.getValue(), option.getProduct().getId());
  }
}