package gift.application.product.service;

import gift.model.product.Product;
import gift.global.validate.NotFoundException;
import gift.model.product.SearchType;
import gift.repository.product.CategoryRepository;
import gift.repository.product.ProductRepository;
import gift.application.product.dto.ProductCommand;
import gift.application.product.dto.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductModel.Info getProduct(Long id) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        return ProductModel.Info.from(product);
    }

    @Transactional
    public ProductModel.Info createProduct(ProductCommand.Register command) {
        var category = categoryRepository.findById(command.categoryId())
            .orElseThrow(() -> new NotFoundException("Category not found"));
        var product = command.toEntity(category);
        productRepository.save(product);
        return ProductModel.Info.from(product);
    }

    @Transactional
    public ProductModel.Info updateProduct(Long id, ProductCommand.Update command) {
        var product = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        product.update(command.name(), command.price(), command.imageUrl());
        return ProductModel.Info.from(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ProductModel.InfoWithOption> getProductsPaging(
        SearchType searchType,
        String searchValue,
        Pageable pageable
    ) {

        Page<Product> productPage = switch (searchType) {
            case NAME -> productRepository.findByNameContaining(searchValue, pageable);
            case PRICE -> productRepository.findAllOrderByPrice(pageable);
            case CATEGORY -> productRepository.findByCategoryId(searchValue, pageable);
            default -> productRepository.findAll(pageable);
        };

        return productPage.map(ProductModel.InfoWithOption::from);
    }
}

