package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.request.OptionRequestDto;
import gift.dto.request.ProductRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.KakaoInNameException;
import gift.repository.category.CategoryRepository;
import gift.repository.product.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static gift.exception.exceptionMessage.ExceptionMessage.CATEGORY_NOT_FOUND;
import static gift.exception.exceptionMessage.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final OptionService optionService;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    @Transactional
    public ProductResponseDto addProduct(ProductRequestDto productDto, OptionRequestDto optionRequestDto) {
        checkNameInKakao(productDto);

        Category category = categoryRepository.findById(productDto.categoryId()).orElseThrow(() -> new EntityNotFoundException("해당 카테고리는 존재하지 않습니다."));

        Product product = new Product.Builder()
                .name(productDto.name())
                .price(productDto.price())
                .imageUrl(productDto.imageUrl())
                .category(category)
                .build();

        product.addCategory(category);

        Product savedProduct = productRepository.save(product);

        optionService.saveOption(optionRequestDto, savedProduct.getId());

        return ProductResponseDto.from(savedProduct);
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        return ProductResponseDto.from(product);
    }

    public List<ProductResponseDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> findProducts(Pageable pageable) {
        return productRepository.findAll(pageable).stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto) {
        checkNameInKakao(productRequestDto);

        Product findProduct = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));
        Category category = categoryRepository.findById(productRequestDto.categoryId()).orElseThrow(() -> new EntityNotFoundException(CATEGORY_NOT_FOUND));

        findProduct.update(productRequestDto, category);

        return ProductResponseDto.from(findProduct);
    }

    @Transactional
    public ProductResponseDto deleteProduct(Long id) {
        Product findProduct = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PRODUCT_NOT_FOUND));

        productRepository.delete(findProduct);

        return ProductResponseDto.from(findProduct);
    }

    private void checkNameInKakao(ProductRequestDto productDto) {
        if (productDto.name().contains("카카오")) {
            throw new KakaoInNameException();
        }
    }
}