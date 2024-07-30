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
    public void addProductOption(Long id, CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        Product product = productRepository.findById(id);

        product.addProductOption(createProductOptionRequestDTO);

        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Long id, String name, Double price, String imageUrl) {
        Product product = productRepository.findById(id);
        product.setName(name);
        product.setPrice(price);
        product.setImageUrl(imageUrl);
        productRepository.save(product);
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

    private void validatePrice(Double price) {
        if (price == null) {
            throw new ProductException(ErrorCode.INVALID_PRICE);
        }
        if (price < 0) {
            throw new ProductException(ErrorCode.NEGATIVE_PRICE);
        }
    }

    public Page<ProductListResponse> getProduct(Pageable pageable) {
        Page<Product> all = productRepository.findAll(pageable);

        return all.map(product -> new ProductListResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl()));
    }

    public class ProductListResponse {
        private Long id;
        private String name;
        private Double price;
        private String imageUrl;

        public ProductListResponse(Long id, String name, Double price, String imageUrl) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.imageUrl = imageUrl;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Double getPrice() {
            return price;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

}
