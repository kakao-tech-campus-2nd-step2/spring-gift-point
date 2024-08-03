package gift.doamin.product.service;

import gift.doamin.category.entity.Category;
import gift.doamin.category.exception.CategoryNotFoundException;
import gift.doamin.category.repository.JpaCategoryRepository;
import gift.doamin.product.dto.OptionRequest;
import gift.doamin.product.dto.ProductCreateRequest;
import gift.doamin.product.dto.ProductResponse;
import gift.doamin.product.dto.ProductUpdateRequest;
import gift.doamin.product.entity.Options;
import gift.doamin.product.entity.Product;
import gift.doamin.product.exception.NotEnoughAutorityException;
import gift.doamin.product.exception.ProductNotFoundException;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final JpaProductRepository productRepository;
    private final JpaUserRepository userRepository;
    private final JpaCategoryRepository categoryRepository;

    public ProductService(JpaProductRepository productRepository,
        JpaUserRepository userRepository, JpaCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductResponse create(Long userId, ProductCreateRequest productCreateRequest) {
        if (productCreateRequest.getName().contains("카카오")) {
            throw new NotEnoughAutorityException("'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findById(productCreateRequest.getCategory_id())
            .orElseThrow(CategoryNotFoundException::new);

        Product product = new Product(user, category, productCreateRequest.getName(),
            productCreateRequest.getPrice(), productCreateRequest.getImageUrl());

        Options options = new Options(productCreateRequest.getOptions().stream()
            .map(OptionRequest::toEntity)
            .toList());
        options.toList().forEach(product::addOption);

        Product actual = productRepository.save(product);

        return new ProductResponse(actual);
    }

    public Page<ProductResponse> getPage(Pageable pageable, Long categoryId) {
        if (categoryId.equals(-1L)) {
            return productRepository.findAll(pageable).map(ProductResponse::new);
        }
        return productRepository.findAllByCategoryId(categoryId, pageable)
            .map(ProductResponse::new);
    }

    public Page<ProductResponse> getPage(int pageNum) {
        return this.getPage(PageRequest.of(pageNum, 5), -1L);
    }

    public ProductResponse readOne(Long id) {

        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        return new ProductResponse(product);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.name, #productId)")
    public ProductResponse update(Long productId, ProductUpdateRequest updateRequest) {

        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);

        Category category = categoryRepository.findById(updateRequest.getCategory_id())
            .orElseThrow(CategoryNotFoundException::new);
        product.updateAll(updateRequest, category);
        return new ProductResponse(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.name, #id)")
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public boolean isOwner(Long userId, Long productId) {
        return productRepository.findById(productId)
            .map(product -> product.getUser().getId().equals(userId))
            .orElseThrow(ProductNotFoundException::new);
    }
}
