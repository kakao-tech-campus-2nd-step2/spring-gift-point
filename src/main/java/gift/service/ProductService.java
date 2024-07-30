package gift.service;

import gift.dto.CategoryDTO;
import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishlistRepository wishlistRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, WishlistRepository wishlistRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishlistRepository = wishlistRepository;
    }

    public Page<ProductDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductDTO::new);
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        return new ProductDTO(product);
    }

    @Transactional
    public void saveProduct(ProductDTO productDTO) {
        // 상품 이름에 '카카오'가 포함되어 있으면 예외 발생
        if (productDTO.getName().contains("카카오")) {
            throw new IllegalArgumentException("상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의하세요.");
        }

        CategoryDTO categoryDTO = productDTO.getCategory();
        if (categoryDTO == null || categoryDTO.getName() == null) {
            throw new IllegalArgumentException("Category must not be null");
        }

        Category category = categoryRepository.findByName(categoryDTO.getName());
        if (category == null) {
            throw new IllegalArgumentException("Invalid category name: " + categoryDTO.getName());
        }

        Product product = new Product(null, productDTO.getName(), productDTO.getPrice(), productDTO.getImageUrl(), category);

        // 상품에 최소 하나 이상의 옵션이 있는지 검증
        if (productDTO.getOptions() == null || productDTO.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one option.");
        }

        product.setOptions(productDTO.getOptions().stream()
                .map(optionDTO -> new Option(null, optionDTO.getName(), optionDTO.getQuantity(), product))
                .collect(Collectors.toSet()));

        productRepository.save(product);
    }

    @Transactional
    public void updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        // 상품 이름에 '카카오'가 포함되어 있으면 예외 발생
        if (productDTO.getName().contains("카카오")) {
            throw new IllegalArgumentException("상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의하세요.");
        }

        Category category = categoryRepository.findByName(productDTO.getCategory().getName());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategory(category);

        // 상품에 최소 하나 이상의 옵션이 있는지 검증
        if (productDTO.getOptions() == null || productDTO.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one option.");
        }

        product.setOptions(productDTO.getOptions().stream()
                .map(optionDTO -> new Option(optionDTO.getId(), optionDTO.getName(), optionDTO.getQuantity(), product))
                .collect(Collectors.toSet()));

        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        wishlistRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }
}