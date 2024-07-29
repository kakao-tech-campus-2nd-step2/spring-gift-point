package gift.service;

import gift.dto.category.CategoryInformation;
import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.exception.InvalidProductNameWithKAKAOException;
import gift.exception.NotFoundElementException;
import gift.model.Category;
import gift.model.MemberRole;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
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

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, WishProductService wishProductService, OptionRepository optionRepository, OptionService optionService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.wishProductService = wishProductService;
        this.optionService = optionService;
    }

    public ProductResponse addProduct(ProductRequest productRequest, MemberRole memberRole) {
        productNameValidation(productRequest, memberRole);
        var product = saveProductWithProductRequest(productRequest);
        optionService.makeDefaultOption(product);
        return getProductResponseFromProduct(product);
    }

    public void updateProduct(Long id, ProductRequest productRequest) {
        var product = findProductById(id);
        updateProductWithProductRequest(product, productRequest);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        var product = findProductById(id);
        return getProductResponseFromProduct(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .stream()
                .map(this::getProductResponseFromProduct)
                .toList();
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

    private Product saveProductWithProductRequest(ProductRequest productRequest) {
        var category = categoryRepository.findById(productRequest.categoryId())
                .orElseThrow(() -> new NotFoundElementException(productRequest.categoryId() + "를 가진 상품 카테고리가 존재하지 않습니다."));
        var product = new Product(productRequest.name(), productRequest.price(), productRequest.imageUrl(), category);
        return productRepository.save(product);
    }

    private void updateProductWithProductRequest(Product product, ProductRequest productRequest) {
        product.updateProductInfo(productRequest.name(), productRequest.price(), productRequest.imageUrl());
        productRepository.save(product);
    }

    private void productNameValidation(ProductRequest productRequest, MemberRole memberRole) {
        if (!productRequest.name().contains("카카오")) return;
        if (memberRole.equals(MemberRole.ADMIN)) return;
        throw new InvalidProductNameWithKAKAOException("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
    }

    private ProductResponse getProductResponseFromProduct(Product product) {
        var categoryInformation = getCategoryInformationFromCategory(product.getCategory());
        return ProductResponse.of(product.getId(), product.getName(), product.getPrice(), product.getImageUrl(), categoryInformation);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 상품옵션이 존재하지 않습니다."));
    }

    private CategoryInformation getCategoryInformationFromCategory(Category category) {
        return CategoryInformation.of(category.getId(), category.getName());
    }
}
