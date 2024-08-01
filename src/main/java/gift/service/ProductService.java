package gift.service;

import gift.dto.ProductDto;
import gift.exception.CustomNotFoundException;
import gift.exception.KakaoValidationException;
import gift.exception.StringValidationException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;

  @Autowired
  public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
  }

  public Page<ProductDto> findAll(Pageable pageable) {
    Page<Product> productPage = productRepository.findAll(pageable);
    List<ProductDto> productDtos = productPage.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

    return new PageImpl<>(productDtos, pageable, productPage.getTotalElements());
  }

  public Optional<ProductDto> findById(Long id) {
    return productRepository.findById(id).map(this::convertToDto);
  }

  public ProductDto save(ProductDto productDto) {
    validate(productDto);
    Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new CustomNotFoundException("Category not found"));
    Product product = new Product(productDto.getName(),  productDto.getPrice(), productDto.getImageUrl(),category);
    Product savedProduct = productRepository.save(product);
    return convertToDto(savedProduct);
  }

  public Page<ProductDto> createProduct(ProductDto productDto, Pageable pageable) {
    validate(productDto);
    Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new CustomNotFoundException("Category not found"));
    Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl(), category);
    Product savedProduct = productRepository.save(product);
    List<ProductDto> productDtos = Collections.singletonList(convertToDto(savedProduct));
    return new PageImpl<>(productDtos, pageable, 1);
  }

  public ProductDto createProduct(ProductDto productDto) {
    validate(productDto);
    Category category = categoryRepository.findById(productDto.getCategoryId())
            .orElseThrow(() -> new CustomNotFoundException("Category not found"));
    Product product = new Product(productDto.getName(), productDto.getPrice(), productDto.getImageUrl(), category);
    Product savedProduct = productRepository.save(product);
    return convertToDto(savedProduct);
  }

  public ProductDto updateProduct(Long id, ProductDto productDetails) {
    if (id == null) {
      throw new IllegalArgumentException("The given id must not be null");
    }
    validate(productDetails);
    return productRepository.findById(id).map(product -> {
      Category category = categoryRepository.findById(productDetails.getCategoryId())
              .orElseThrow(() -> new CustomNotFoundException("Category not found"));
      product.updateFromDto(productDetails.getName(), productDetails.getPrice(), productDetails.getImageUrl(), category);
      Product updatedProduct = productRepository.save(product);
      return convertToDto(updatedProduct);
    }).orElseThrow(() -> new CustomNotFoundException("Product not found"));
  }

  public void deleteById(Long id) {
    productRepository.deleteById(id);
  }

  private ProductDto convertToDto(Product product) {
    return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
  }

  private void validate(ProductDto productDto) {
    String name = productDto.getName();
    if (name.contains("카카오")) {
      throw new KakaoValidationException("상품 이름에 '카카오'를 포함하려면 담당 MD와 협의가 필요합니다.");
    }
    if (!name.matches("^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&\\/]*$")) {
      throw new StringValidationException("허용되지 않은 특수기호는 사용할 수 없습니다. 허용된 특수기호: ( ), [ ], +, -, &, /, _");
    }
  }
}