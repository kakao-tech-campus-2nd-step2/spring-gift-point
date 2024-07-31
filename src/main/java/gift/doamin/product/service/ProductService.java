package gift.doamin.product.service;

import gift.doamin.category.entity.Category;
import gift.doamin.category.exception.CategoryNotFoundException;
import gift.doamin.category.repository.JpaCategoryRepository;
import gift.doamin.product.dto.OptionForm;
import gift.doamin.product.dto.ProductForm;
import gift.doamin.product.dto.ProductParam;
import gift.doamin.product.entity.Options;
import gift.doamin.product.entity.Product;
import gift.doamin.product.exception.NotEnoughAutorityException;
import gift.doamin.product.exception.ProductNotFoundException;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

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

    public ProductParam create(ProductForm productForm) {
        if (productForm.getName().contains("카카오")) {
            throw new NotEnoughAutorityException("'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }

        User user = userRepository.findById(productForm.getUserId())
            .orElseThrow(UserNotFoundException::new);

        Category category = categoryRepository.findById(productForm.getCategory_id())
            .orElseThrow(CategoryNotFoundException::new);

        Product product = new Product(user, category, productForm.getName(), productForm.getPrice(),
            productForm.getImageUrl());

        Options options = new Options(productForm.getOptions().stream()
            .map(OptionForm::toEntity)
            .toList());
        options.toList().forEach(product::addOption);

        Product actual = productRepository.save(product);

        return new ProductParam(actual);
    }

    public Page<ProductParam> getPage(int pageNum) {

        Pageable pageable = PageRequest.of(pageNum, 5);
        return productRepository.findAll(pageable).map(ProductParam::new);
    }

    public ProductParam readOne(Long id) {

        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        return new ProductParam(product);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @productService.isOwner(authentication.name, #id)")
    public ProductParam update(Long id, ProductForm productForm) {

        Optional<Product> target = productRepository.findById(id);
        if (target.isEmpty()) {
            return create(productForm);
        }

        Product product = target.get();

        Category category = categoryRepository.findById(productForm.getCategory_id())
            .orElseThrow(CategoryNotFoundException::new);
        product.updateAll(productForm, category);
        return new ProductParam(productRepository.save(product));
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
