package gift.service;

import static gift.controller.product.ProductMapper.from;
import static gift.controller.product.ProductMapper.toProductResponse;

import gift.controller.product.ProductMapper;
import gift.controller.product.ProductRequest;
import gift.controller.product.ProductResponse;
import gift.domain.Product;
import gift.exception.ProductAlreadyExistsException;
import gift.exception.ProductHasNotOptionException;
import gift.exception.ProductNotExistsException;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductResponse> productResponses = productPage.stream()
            .filter(product -> product.getOptions() != null && !product.getOptions().isEmpty())
            .map(ProductMapper::toProductResponse).toList();
        return new PageImpl<>(productResponses, pageable, productPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductResponse(UUID id) {
        Product target = productRepository.findById(id).orElseThrow(ProductNotExistsException::new);
        if(target.getOptions().isEmpty()) throw new ProductHasNotOptionException();
        return toProductResponse(target);
    }

    @Transactional(readOnly = true)
    public Product find(UUID id) {
        return productRepository.findById(id).orElseThrow(ProductNotExistsException::new);
    }

    @Transactional
    public ProductResponse save(ProductRequest product) {
        productRepository.findByNameAndPriceAndImageUrl(product.name(), product.price(),
            product.imageUrl()).ifPresent(p -> {
            throw new ProductAlreadyExistsException();
        });
        return toProductResponse(productRepository.save(from(product, categoryService.findByName(product.categoryName()))));
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest product) {
        Product target = productRepository.findById(id).orElseThrow(ProductNotExistsException::new);
        target.updateDetails(product.name(), product.price(), product.imageUrl());
        return toProductResponse(target);
    }

    @Transactional
    public void delete(UUID id) {
        productRepository.findById(id).orElseThrow(ProductNotExistsException::new);
        productRepository.deleteById(id);
    }
}