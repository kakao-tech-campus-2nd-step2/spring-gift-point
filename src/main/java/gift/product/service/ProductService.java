package gift.product.service;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import gift.option.domain.Option;
import gift.option.repository.OptionRepository;
import gift.product.domain.Product;
import gift.product.dto.ProductResponseListDto;
import gift.product.dto.ProductServiceDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private static final int PAGE_SIZE = 20;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.optionRepository = optionRepository;
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

    @Transactional
    public Product createProduct(ProductServiceDto productServiceDto) {
        Category category = categoryService.getCategoryById(productServiceDto.categoryId());
        Product product = productRepository.save(productServiceDto.toProduct(category));
        List<Option> options = productServiceDto.options();
        setProductInOptions(options, product);
        optionRepository.saveAll(options);
        return product;
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

    private void setProductInOptions(List<Option> options, Product product) {
        for (Option option : options) {
            option.setProduct(product);
        }
    }

    private void validateProductExists(Long id) {
        productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

}
