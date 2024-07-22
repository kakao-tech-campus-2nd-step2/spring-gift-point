package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import gift.product.domain.Product;
import gift.product.dto.ProductResponseListDto;
import gift.product.dto.ProductServiceDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private static final int PAGE_SIZE = 10;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductResponseListDto getProductsByPage(int page) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, PAGE_SIZE));
        return ProductResponseListDto.productPageToProductResponseListDto(products);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(ProductServiceDto productServiceDto) {
        Category category = categoryService.getCategoryById(productServiceDto.categoryId());
        return productRepository.save(productServiceDto.toProduct(category));
    }

    public Product updateProduct(ProductServiceDto productServiceDto) {
        validateProductExists(productServiceDto.id());
        Category category = categoryService.getCategoryById(productServiceDto.categoryId());
        return productRepository.save(productServiceDto.toProduct(category));
    }

    public void deleteProduct(Long id) {
        validateProductExists(id);
        productRepository.deleteById(id);
    }

    private void validateProductExists(Long id) {
        productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

}
