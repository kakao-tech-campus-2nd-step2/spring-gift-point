package gift.services;

import gift.classes.Exceptions.ProductException;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.ProductDto;
import gift.dto.RequestOptionDto;
import gift.dto.RequestProductDto;
import gift.repositories.CategoryRepository;
import gift.repositories.OptionRepository;
import gift.repositories.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final OptionService optionService;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(OptionService optionService, ProductRepository productRepository,
        OptionRepository optionRepository, CategoryRepository categoryRepository) {
        this.optionService = optionService;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
        this.categoryRepository = categoryRepository;
    }

    // 모든 제품 조회
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(Product::toProductDto).toList();

        return productDtos;
    }

    //Page 반환, 모든 제품 조회
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductDto> productDtos = products.map(Product::toProductDto);

        return productDtos;
    }

    // 특정 제품 조회
    public ProductDto getProductById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Product not found with id " + id));

        ProductDto productDto = product.toProductDto();
        return productDto;
    }

    // 제품 추가
    public ProductDto addProduct(@Valid RequestProductDto requestProductDto) {
        if (requestProductDto.getOptionDtos().isEmpty()) {
            throw new ProductException("One or more options are required to add a product.");
        }
        Product product = toProductEntity(requestProductDto);

        for (RequestOptionDto requestOptionDto : requestProductDto.getOptionDtos()) {
            optionService.addOption(product.getId(), requestOptionDto);
        }
        List<Option> options = optionRepository.findAllByProductId(product.getId());
        product.setOptions(options);

        productRepository.save(product);

        return product.toProductDto();
    }

    // 제품 수정
    public ProductDto updateProduct(@Valid RequestProductDto requestProductDto) {
        Product product = productRepository.findById(requestProductDto.getId())
            .orElseThrow(() -> new NoSuchElementException(
                "Product not found with id " + requestProductDto.getId()));

        Category category = categoryRepository.findById(requestProductDto.getCategoryDto().getId())
            .orElseThrow(() -> new NoSuchElementException(
                "Category not found"));

        product.update(requestProductDto.getName(), requestProductDto.getPrice(),
            requestProductDto.getImageUrl(), category);
        return product.toProductDto();
    }

    // 제품 삭제
    public void deleteProduct(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new NoSuchElementException("Product not found with id " + id);
        }

        productRepository.deleteById(id);
    }

    public Product toProductEntity(RequestProductDto requestProductDto) {
        Category category = categoryRepository.findByName(
            requestProductDto.getCategoryDto().getName());
        return new Product(requestProductDto.getName(), requestProductDto.getPrice(),
            requestProductDto.getImageUrl(), category);

    }
}

