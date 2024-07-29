package gift.product.infrastructure.persistence.repository;

import gift.core.PagedDto;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductCategoryRepository;
import gift.product.infrastructure.persistence.entity.ProductCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {
    private final JpaProductCategoryRepository jpaProductCategoryRepository;

    public ProductCategoryRepositoryImpl(
            JpaProductCategoryRepository jpaProductCategoryRepository
    ) {
        this.jpaProductCategoryRepository = jpaProductCategoryRepository;
    }

    public PagedDto<ProductCategory> findAll(Pageable pageable) {
        Page<ProductCategory> categories = jpaProductCategoryRepository
                .findAll(pageable)
                .map(ProductCategoryEntity::toDomain);
        return new PagedDto<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                categories.getTotalElements(),
                categories.getTotalPages(),
                categories.getContent()
        );
    }

    public ProductCategory save(ProductCategory category) {
        return jpaProductCategoryRepository
                .save(ProductCategoryEntity.fromDomain(category))
                .toDomain();
    }

    public void remove(Long id) {
        jpaProductCategoryRepository.deleteById(id);
    }

    public Optional<ProductCategory> findById(Long id) {
        return jpaProductCategoryRepository
                .findById(id)
                .map(ProductCategoryEntity::toDomain);
    }

    @Override
    public Optional<ProductCategory> findByName(String name) {
        return jpaProductCategoryRepository
                .findByName(name)
                .map(ProductCategoryEntity::toDomain);
    }
}
