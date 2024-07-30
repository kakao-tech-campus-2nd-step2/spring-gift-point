package gift.service;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
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

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product addProduct(ProductDto productDto) {
        Category category = getCategoryById(productDto.getCategoryId());
        List<Option> options = convertOptions(productDto.getOptions());
        Product product = new Product(
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl(),
                category,
                options
        );
        productRepository.save(product);
        return product;
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        Category category = getCategoryById(productDto.getCategoryId());
        List<Option> options = convertOptions(productDto.getOptions());
        Product updateProduct = new Product(
                id,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getImageUrl(),
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

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public void updateProductCategoryToNone(Long id) {
        Category category = getCategoryById(id);
        Category noneCategory = getCategoryById(1L);

        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            Product updateProduct = new Product(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    noneCategory,
                    product.getOptions()
            );
            productRepository.save(updateProduct);
        }
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
