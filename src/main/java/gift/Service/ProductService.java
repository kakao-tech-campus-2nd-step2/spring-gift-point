package gift.Service;

import gift.ConverterToDto;
import gift.DTO.Category;
import gift.DTO.CategoryDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.Repository.ProductRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Page<ProductDto> getAllProducts(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);
    Page<ProductDto> productDtos = products.map(ConverterToDto::convertToProductDto);

    return productDtos;
  }

  public ProductDto getProductById(Long id) {
    Product product = (productRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다.", 1)));

    return ConverterToDto.convertToProductDto(product);
  }

  public ProductDto addProduct(ProductDto productDto) {
    CategoryDto categoryDto = productDto.getCategoryDto();
    Category category = new Category(categoryDto.getId(), categoryDto.getName(),
      categoryDto.getColor(), categoryDto.getImageUrl(), categoryDto.getDescription());

    Product product = new Product(productDto.getName(),
      productDto.getPrice(), productDto.getImageUrl(), category);

    productRepository.save(product);

    return productDto;
  }

  public ProductDto updateProduct(Long id, ProductDto updatedProductDto) {
    Product existingProduct = productRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다.", 1));

    CategoryDto updateCategoryDto = updatedProductDto.getCategoryDto();
    Category updateCategory = new Category(updatedProductDto.getId(), updatedProductDto.getName(),
      updateCategoryDto.getColor(), updatedProductDto.getImageUrl(),
      updateCategoryDto.getDescription());

    Product newProduct = new Product(id, updatedProductDto.getName(), updatedProductDto.getPrice(),
      updatedProductDto.getImageUrl(), updateCategory);

    productRepository.save(newProduct);

    return ConverterToDto.convertToProductDto(newProduct);
  }

  public ProductDto deleteProduct(@PathVariable Long id) {
    Product existingProduct = productRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));
    productRepository.deleteById(id);

    return ConverterToDto.convertToProductDto(existingProduct);
  }

}
