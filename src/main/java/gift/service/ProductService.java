package gift.service;

import gift.dto.ProductRequestDto;
import gift.dto.ProductUpdateDto;
import gift.repository.ProductRepository;
import gift.vo.Category;
import gift.vo.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }
    
    private Category getCategory(Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    /**
     * 페이지네이션 적용된 모든 상품 가져오는 메소드
     * @return Page<Product></Product>
     */
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
    }

    public void addProduct(@Valid ProductRequestDto productRequestDto) {
        Product product = productRequestDto.toProduct(getCategory(productRequestDto.categoryId()));
        optionService.addOption(product, productRequestDto.options());
        Product.validateOptionsExist(product.getOptions());
        productRepository.save(product);
    }

    public void updateProduct(@Valid ProductUpdateDto updateDto) {
        Product updateProduct = updateDto.toProduct(getCategory(updateDto.categoryId()));
        productRepository.findById(updateProduct.getId())
                .orElseThrow(() -> new IllegalArgumentException("수정하려는 상품을 찾을 수 없습니다."));
        productRepository.save(updateProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제하려는 상품을 찾을 수 없습니다."));
        productRepository.deleteById(id);
    }
}
