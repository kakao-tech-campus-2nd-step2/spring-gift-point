package gift.service;

import gift.dto.OptionDto;
import gift.dto.ProductDetailDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishlistRepository wishlistRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository, WishlistRepository wishlistRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishlistRepository = wishlistRepository;
    }

    public Page<ProductResponseDto> getApiProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> {
            ProductResponseDto dto = new ProductResponseDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl()
            );
            return dto;
        });
    }

    public Page<Product> getWebProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(product -> {
            Product dto = new Product(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    product.getCategory(),
                    product.getOptions()
            );
            return dto;
        });
    }

    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Category category = getCategoryById(productRequestDto.getCategoryId());
        List<Option> options = convertOptions(productRequestDto.getOptions());
        Product product = new Product(
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl(),
                category,
                options
        );
        Product savedProduct = productRepository.save(product);
        return convertToResponseDto(savedProduct);
    }

    public Product updateProduct(Long id, ProductRequestDto productRequestDto) {
        Category category = getCategoryById(productRequestDto.getCategoryId());
        List<Option> options = convertOptions(productRequestDto.getOptions());
        Product updateProduct = new Product(
                id,
                productRequestDto.getName(),
                productRequestDto.getPrice(),
                productRequestDto.getImageUrl(),
                category,
                options
        );
        return productRepository.save(updateProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        wishlistRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    public ProductDetailDto getApiProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
        return convertToDetailDto(product);
    }

    public Product getWebProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
        return product;
    }

    private ProductDetailDto convertToDetailDto(Product product) {
        ProductDetailDto dto = new ProductDetailDto(
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
        return dto;
    }

    private ProductResponseDto convertToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl());
        return dto;
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 카테고리입니다."));
    }

    private List<Option> convertOptions(List<OptionDto> optionDtos) {
        return optionDtos.stream()
                .map(optionDto -> new Option(optionDto.getName(), optionDto.getQuantity()))
                .collect(Collectors.toList());
    }
}
