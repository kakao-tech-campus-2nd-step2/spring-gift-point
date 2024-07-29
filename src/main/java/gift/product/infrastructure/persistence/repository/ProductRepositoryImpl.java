package gift.product.infrastructure.persistence.repository;

import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.product.ProductCategory;
import gift.core.domain.product.ProductRepository;
import gift.product.infrastructure.persistence.entity.ProductCategoryEntity;
import gift.product.infrastructure.persistence.entity.ProductEntity;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;

    @Autowired
    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaProductRepository
                .findById(id)
                .map(ProductEntity::toDomain);
    }

    @Override
    public boolean exists(Long id) {
        return jpaProductRepository.existsById(id);
    }

    @Override
    public void save(@Nonnull Product product) {
        jpaProductRepository.save(ProductEntity.fromDomain(product));
    }

    @Override
    public long size() {
        return jpaProductRepository.count();
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository
                .findAll()
                .stream()
                .map(ProductEntity::toDomain)
                .toList();
    }

    public PagedDto<Product> findAll(Pageable pageable) {
        Page<Product> pagedProducts = jpaProductRepository
                .findAll(pageable)
                .map(ProductEntity::toDomain);
        return new PagedDto<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pagedProducts.getTotalElements(),
                pagedProducts.getTotalPages(),
                pagedProducts.getContent()
        );
    }

    @Override
    public void remove(Long id) {
        jpaProductRepository.deleteById(id);
    }
}
