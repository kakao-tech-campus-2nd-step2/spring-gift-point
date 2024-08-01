package gift.services;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.PageInfoDto;
import gift.dto.ProductDto;
import gift.dto.ProductPageDto;
import gift.dto.RequestProductDto;
import gift.repositories.CategoryRepository;
import gift.repositories.OptionRepository;
import gift.repositories.ProductRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public ProductPageDto getAllProductsByCategoryId(int page, int size, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Product> products = productRepository.findAllByCategoryId(categoryId, pageable);
        List<ProductDto> productDtoList = products
            .stream()
            .map(Product::toProductDto)
            .toList();
        PageInfoDto pageInfoDto = new PageInfoDto(page, products.getTotalElements(),
            products.getTotalPages());
        return new ProductPageDto(pageInfoDto, productDtoList);
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
        Product product = toProductEntity(requestProductDto);

        productRepository.save(product);

        return product.toProductDto();
    }

    // 제품 수정
    public ProductDto updateProduct(@Valid RequestProductDto requestProductDto, Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException(
                "Product not found with id " + id));

        Category category = categoryRepository.findByName(requestProductDto.getCategoryName());

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
            requestProductDto.getCategoryName());

        return new Product(requestProductDto.getName(), requestProductDto.getPrice(),
            requestProductDto.getImageUrl(), category);
    }
}

