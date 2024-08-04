package gift.product.application;

import gift.product.domain.*;
import gift.product.exception.ProductException;
import gift.product.infra.ProductRepository;
import gift.util.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }


    private static final int MAX_PRODUCT_NAME_LENGTH = 15;
    private static final String RESERVED_KEYWORD = "카카오";

    @Transactional
    public Long saveProduct(CreateProductRequestDTO createProductRequestDTO) {
        Category category = categoryService.getCategoryByName(createProductRequestDTO.getCategory());
        Product product = new Product(createProductRequestDTO, category);
        validateProduct(product);

        return productRepository.save(product).getId();
    }

    @Transactional
    public Product addProductOption(Long id, CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        Product product = productRepository.findById(id);
//        product.addProductOption(createProductOptionRequestDTO);
        return productRepository.updateProductOption(product, createProductOptionRequestDTO);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private void validateProduct(Product product) {
        validateName(product.getName());
        validatePrice(product.getPrice());
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ProductException(ErrorCode.INVALID_NAME);
        }
        if (name.length() > MAX_PRODUCT_NAME_LENGTH) {
            throw new ProductException(ErrorCode.NAME_TOO_LONG);
        }

        if (name.contains(RESERVED_KEYWORD)) {
            throw new ProductException(ErrorCode.NAME_HAS_RESTRICTED_WORD);
        }
    }

    private void validatePrice(Long price) {
        if (price == null) {
            throw new ProductException(ErrorCode.INVALID_PRICE);
        }
        if (price < 0) {
            throw new ProductException(ErrorCode.NEGATIVE_PRICE);
        }
    }

    public ProductListResponse getProduct(Pageable pageable) {
        Page<Product> all = productRepository.findAll(pageable);
        return new ProductListResponse(
                all.getContent().stream()
                        .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()))
                        .toList(),
                all.getNumber(),
                all.getTotalElements(),
                all.getNumberOfElements(),
                all.isLast()
        );
    }

    public ProductListResponse getProductByCategory(Long categoryId, Pageable pageable) {
        Page<Product> all = productRepository.findByCategoryId(categoryId, pageable);
        return new ProductListResponse(
                all.getContent().stream()
                        .map(product -> new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()))
                        .toList(),
                all.getNumber(),
                all.getTotalElements(),
                all.getNumberOfElements(),
                all.isLast()
        );
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id);
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public List<ProductOptionData> getProductOptions(Long id) {
        List<ProductOption> productOptions = productRepository.findProductOptionsByProductId(id);
        return productOptions.stream()
                .map(productOption -> new ProductOptionData(productOption.getId(), productOption.getName(), productOption.getQuantity(), productOption.getProduct().getId()))
                .toList();
    }

    public Map<Long, Product> getProductsByIds(List<Long> productIds) {
        return productRepository.getProductsByIds(productIds);
    }


    public class ProductOptionData {
        private Long id;
        private String name;
        private Long quantity;
        private Long productId;

        public ProductOptionData(Long id, String name, Long quantity, Long productId) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.productId = productId;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Long getQuantity() {
            return quantity;
        }

        public Long getProductId() {
            return productId;
        }
    }

}
