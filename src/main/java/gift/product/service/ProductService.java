package gift.product.service;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.exception.ForbiddenWordException;
import gift.exception.NonIntegerPriceException;
import gift.exception.OptionNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.exception.ResourceNotFoundException;
import gift.option.repository.OptionRepository;
import gift.product.dto.ProductRequest;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return product.orElse(null);
    }

    public Product createProduct(@Valid ProductRequest productRequest)
        throws NonIntegerPriceException {
        if (productRequest.getName().contains("카카오")) {
            throw new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
        }
        var priceValue = new BigDecimal(productRequest.getPrice());
        if (priceValue.scale() > 0 && priceValue.stripTrailingZeros().scale() > 0) {
            throw new NonIntegerPriceException("상품 가격은 소수점이 없어야 합니다");
        }
        var newProduct = new Product(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl());
        var category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 카테고리입니다."));
        var option = optionRepository.findById(productRequest.getOptionId()).orElse(null);

        newProduct.setCategory(category);
        if (option != null) {
            newProduct.getOptionList().add(option);
        }
        category.getProductList().add(newProduct);
        return productRepository.save(newProduct);
    }

    public Product updateProduct(Long productId, ProductRequest productRequest) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("업는 상품입니다."));

        if (product.getName().contains("카카오")) {
            throw new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
        }

        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        Category category = categoryRepository.findById(productRequest.getCategoryId())
            .orElseThrow(() -> new IllegalArgumentException("없는 카테고리 ID 입니다."));
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product patchProduct(Long id, Map<String, Object> updates) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new ProductNotFoundException("수정할 상품이 존재하지 않습니다.");
        }
        applyUpdates(existingProduct.orElse(null), updates);
        return productRepository.save(existingProduct.get());
    }

    @Transactional
    public List<Optional<Product>> patchProducts(List<Map<String, Object>> updatesList) {
        List<Optional<Product>> updatedProducts = new ArrayList<>();
        for (Map<String, Object> updates : updatesList) {
            try {
                Long id = ((Number) updates.get("id")).longValue();
                if (patchProduct(id, updates) != null) {
                    updatedProducts.add(productRepository.findById(id));
                }
            } catch (ProductNotFoundException | ForbiddenWordException ignored) {
            }
        }
        return updatedProducts;
    }

    private void applyUpdates(Product product, Map<String, Object> updates) {
        updates.forEach((key, value) -> {
            if (!"id".equals(key)) {
                updateProductField(product, key, value);
            }
        });
        if (product.getName().contains("카카오")) {
            throw new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
        }
    }

    private void updateProductField(Product product, String key, Object value) {
        if ("name".equals(key)) {
            product.setName((String) value);
            return;
        }
        if ("price".equals(key)) {
            product.setPrice((Integer) value);
            return;
        }
        if ("imageUrl".equals(key)) {
            product.setImageUrl((String) value);
            return;
        }
        throw new IllegalArgumentException("Invalid field: " + key);
    }


    public boolean deleteProduct(Long id) {
        productRepository.deleteById(id);
        return productRepository.findById(id).isEmpty();
    }

    public Product updateProductByName(String name, @Valid Product updatedProduct) {
        var existingProduct = productRepository.findByName(name);
        if (existingProduct.isEmpty()) {
            throw new ProductNotFoundException("수정할 상품이 존재하지 않습니다!");
        }
        var product = existingProduct.get();
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setImageUrl(updatedProduct.getImageUrl());
        return productRepository.save(product);
    }

    public Page<Product> getProductPages(int pageNumber) {
        var pageable = PageRequest.of(pageNumber, 5, Sort.by("id"));
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductList(int page, int pageSize, String sortProperty,
        Direction direction) {
        Pageable pageable = PageRequest.of(page, pageSize,
            Sort.by(new Order(direction, sortProperty)));
        return productRepository.findAll(pageable);
    }

}
