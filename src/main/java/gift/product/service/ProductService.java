package gift.product.service;

import gift.category.entity.Category;
import gift.exception.BadRequestException;
import gift.exception.DuplicateResourceException;
import gift.exception.ResourceNotFoundException;
import gift.product.dto.OptionRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Autowired
  public ProductService(ProductRepository productRepository,
      CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }


  @Transactional
  public Page<ProductResponseDto> getAllProducts(Long categoryId, Pageable pageable) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new ResourceNotFoundException("해당 카테고리를 찾을 수 없습니다.");
    }

    Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);

    return productPage.map(ProductResponseDto::toDto);
  }

  public ProductResponseDto getProductById(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 상품을 찾을 수 없습니다."));

    return ProductResponseDto.toDto(product);
  }

  @Transactional
  public ProductResponseDto createProduct(@Valid ProductRequestDto productRequestDto,
      @Valid OptionRequestDto optionRequestDto) {

    if (productRepository.existsByName(productRequestDto.name())) {
      throw new DuplicateResourceException("이미 존재하는 상품 이름입니다");
    }

    Category category = categoryRepository.findById(productRequestDto.categoryId())
        .orElseThrow(() -> new ResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));

    Product product = Product.builder()
        .name(productRequestDto.name())
        .price(productRequestDto.price())
        .imageUrl(productRequestDto.imageUrl())
        .category(category)
        .build();

    Option option = Option.builder()
        .name(optionRequestDto.optionName())
        .quantity(
            optionRequestDto.optionQuantity())
        .product(product).build();

    product.addOption(option);

    Product savedProduct = productRepository.save(product);
    return ProductResponseDto.toDto(savedProduct);
  }

  @Transactional
  public ProductResponseDto updateProduct(Long productId,
      @Valid ProductRequestDto productRequestDto) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("해당 상품은 존재하지 않습니다."));

    if (!product.getName().equals(productRequestDto.name()) && productRepository.existsByName(
        productRequestDto.name())) {
      throw new DuplicateResourceException("이미 존재하는 상품 이름입니다.");
    }

    Category category = categoryRepository.findById(productRequestDto.categoryId())
        .orElseThrow(() ->
            new ResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));

    product.update(productRequestDto, category);

    Product updatedProduct = productRepository.save(product);
    return ProductResponseDto.toDto(updatedProduct);
  }


  @Transactional
  public void deleteProduct(Long productId) {
    if (!productRepository.existsById(productId)) {
      throw new ResourceNotFoundException("해당 상품은 존재하지 않습니다.");
    }
    productRepository.deleteById(productId);
  }
}
