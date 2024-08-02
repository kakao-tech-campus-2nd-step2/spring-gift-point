package gift.service;

import gift.domain.Product.ProductRequest;
import gift.domain.Product.ProductResponse;
import gift.entity.CategoryEntity;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.error.AlreadyExistsException;
import gift.error.NotFoundException;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionRepository optionRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    //전체 상품 조회 기능
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageable);
        return productEntities.map(ProductResponse::from);
    }

    //단일 상품 조회 기능
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        return ProductResponse.from(productEntity);

    }

    //이름을 통한 상품 검색 기능
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProduct(String name) {
        List<ProductEntity> productEntities = productRepository.findByNameContaining(name);
        return productEntities.stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }

    //상품 추가 기능
    @Transactional
    public void addProduct(ProductRequest product) {
        validateProductUniqueness(product);
        CategoryEntity categoryEntity = categoryRepository.findById(product.categoryId())
            .orElseThrow(() -> new NotFoundException("Category Not Found"));
        List<OptionEntity> optionEntities = product.optionIds().stream()
            .map(optionRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        ProductEntity productEntity = new ProductEntity(
            product.name(),
            product.price(),
            product.imageUrl(),
            categoryEntity,
            optionEntities
        );
        productRepository.save(productEntity);
    }

    //상품 수정 기능
    @Transactional
    public void updateProduct(Long id, ProductRequest product) {
        validateProductUniqueness(product);
        ProductEntity productEntity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        CategoryEntity categoryEntity = categoryRepository.findById(product.categoryId())
            .orElseThrow(() -> new NotFoundException("Category Not Found"));
        productEntity.updateProductEntity(
            product.name(),
            product.price(),
            product.imageUrl(),
            categoryEntity
        );
        productRepository.save(productEntity);
    }

    //상품 삭제 기능
    @Transactional
    public void deleteProduct(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(productEntity);
    }

    private void validateProductUniqueness(ProductRequest product) {
        if(productRepository.existsByNameAndPriceAndImageUrl(
            product.name(), product.price(), product.imageUrl())) {
            throw new AlreadyExistsException("Already Exists Product");
        }
    }

}
