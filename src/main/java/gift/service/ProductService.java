package gift.service;

import gift.dto.OptionRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductResponse> findAll(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(this::convertToResponse);
    }

    public Page<ProductResponse> findAllByCategoryId(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategoryId(categoryId, pageRequest).map(this::convertToResponse);
    }

    public ProductResponse findById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
        return convertToResponse(product);
    }

    public void save(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Product product = new Product(productRequest.name(), productRequest.price(), productRequest.imageUrl(), category);
        productRepository.save(product);

        List<Option> options = productRequest.options().stream()
                .map(optionRequest -> new Option(optionRequest.name(), optionRequest.quantity(), product))
                .collect(Collectors.toList());

        optionRepository.saveAll(options);
    }

    public void update(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));

        Category category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setImageUrl(productRequest.imageUrl());
        product.setCategory(category);

        productRepository.save(product);
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));

        productRepository.delete(product);
    }

    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Product with id " + id + " not found"));
            productRepository.delete(product);
        }
    }

    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }
}
