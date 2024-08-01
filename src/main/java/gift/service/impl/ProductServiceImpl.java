package gift.service.impl;

import gift.exception.ForbiddenWordException;
import gift.exception.ProductNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.ProductService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public boolean createProduct(@Valid Product product) {
        if (product.getName().contains("카카오")) {
            throw new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
        }
        Product savedProduct = productRepository.save(product);

        // 옵션을 추가하는 로직
        if (product.getOptions() != null && !product.getOptions().isEmpty()) {
            for (Option option : product.getOptions()) {
                option.setProduct(savedProduct);
                optionRepository.save(option);
            }
        }
        return true;
    }

    @Override
    public boolean updateProduct(Long id, @Valid Product product) {
        if (product.getName().contains("카카오")) {
            throw new ForbiddenWordException("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
        }
        return productRepository.findById(id)
            .map(existingProduct -> {
                existingProduct.setName(product.getName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setImageUrl(product.getImageUrl());
                existingProduct.setCategory(product.getCategory());

                optionRepository.deleteAll(existingProduct.getOptions());
                for (Option option : product.getOptions()) {
                    option.setProduct(existingProduct);
                    optionRepository.save(option);
                }

                productRepository.save(existingProduct);
                return true;
            })
            .orElse(false);
    }

    @Override
    @Transactional
    public boolean patchProduct(Long id, Map<String, Object> updates) {
        return productRepository.findById(id)
            .map(existingProduct -> {
                applyUpdates(existingProduct, updates);
                productRepository.save(existingProduct);
                return true;
            })
            .orElse(false);
    }

    @Override
    @Transactional
    public List<Product> patchProducts(List<Map<String, Object>> updatesList) {
        List<Product> updatedProducts = new ArrayList<>();
        for (Map<String, Object> updates : updatesList) {
            try {
                Long id = ((Number) updates.get("id")).longValue();
                if (patchProduct(id, updates)) {
                    updatedProducts.add(productRepository.findById(id).orElseThrow());
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
        if ("category".equals(key)) {
            Long categoryId = Long.valueOf((String) value);
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
            product.setCategory(category);
            return;
        }
        throw new IllegalArgumentException("Invalid field: " + key);
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Product> getProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}
