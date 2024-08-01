package gift.service;

import gift.dto.category.CategoryResponse;
import gift.dto.option.OptionRequest;
import gift.dto.product.ProductAddRequest;
import gift.dto.product.ProductPageResponse;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.exception.InvalidProductNameWithKAKAOException;
import gift.exception.NotFoundElementException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WishProductService wishProductService;
    private final OptionService optionService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, WishProductService wishProductService, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductService = wishProductService;
        this.optionService = optionService;
    }

    public ProductResponse addProduct(ProductAddRequest productAddRequest) {
        productNameValidation(productAddRequest.name());
        var product = saveProductWithProductRequest(productAddRequest);
        makeOptionsWithProductRequest(product, productAddRequest.options());
        return getProductResponseFromProduct(product);
    }

    public void updateProduct(Long id, ProductUpdateRequest productUpdateRequest) {
        productNameValidation(productUpdateRequest.name());
        var product = findProductById(id);
        updateProductWithProductRequest(product, productUpdateRequest);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        var product = findProductById(id);
        return getProductResponseFromProduct(product);
    }

    @Transactional(readOnly = true)
    public ProductPageResponse getProducts(Pageable pageable) {
        var pageResult = productRepository.findAll(pageable);
        var products = pageResult
                .getContent()
                .stream()
                .map(this::getProductResponseFromProduct)
                .toList();
        return new ProductPageResponse(pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(), pageResult.getTotalPages(), products);
    }

    @Transactional(readOnly = true)
    public ProductPageResponse getProducts(Long categoryId, Pageable pageable) {
        var pageResult = productRepository.findAllByCategoryId(categoryId, pageable);
        var products = pageResult
                .getContent()
                .stream()
                .map(this::getProductResponseFromProduct)
                .toList();

        return new ProductPageResponse(pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(), pageResult.getTotalPages(), products);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundElementException("존재하지 않는 상품의 ID 입니다.");
        }
        optionService.deleteAllByProductId(productId);
        wishProductService.deleteAllByProductId(productId);
        productRepository.deleteById(productId);
    }

    public void deleteAllProductWithCategoryId(Long categoryId) {
        var products = productRepository.findAllByCategoryId(categoryId);
        for (var product : products) {
            deleteProduct(product.getId());
        }
    }

    private Product saveProductWithProductRequest(ProductAddRequest productAddRequest) {
        var category = categoryRepository.findById(productAddRequest.categoryId())
                .orElseThrow(() -> new NotFoundElementException(productAddRequest.categoryId() + "를 가진 상품 카테고리가 존재하지 않습니다."));
        var product = new Product(productAddRequest.name(), productAddRequest.price(), productAddRequest.imageUrl(), category);
        return productRepository.save(product);
    }

    private void makeOptionsWithProductRequest(Product product, List<OptionRequest> options) {
        for (var option : options) {
            optionService.addOption(product.getId(), option);
        }
    }

    private void updateProductWithProductRequest(Product product, ProductUpdateRequest productUpdateRequest) {
        product.updateProductInfo(productUpdateRequest.name(), productUpdateRequest.price(), productUpdateRequest.imageUrl());
        productRepository.save(product);
    }

    private void productNameValidation(String name) {
        if (!name.contains("카카오")) return;
        throw new InvalidProductNameWithKAKAOException("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
    }

    private ProductResponse getProductResponseFromProduct(Product product) {
        var categoryResponse = getCategoryResponseFromCategory(product.getCategory());
        var options = optionService.getOptions(product.getId());
        return ProductResponse.of(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), categoryResponse, options);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품옵션이 존재하지 않습니다."));
    }

    private CategoryResponse getCategoryResponseFromCategory(Category category) {
        return CategoryResponse.of(category.getId(), category.getName(), category.getDescription(), category.getColor(), category.getImageUrl());
    }
}
