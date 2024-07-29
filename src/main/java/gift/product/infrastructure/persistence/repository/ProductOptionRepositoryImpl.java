package gift.product.infrastructure.persistence.repository;

import gift.core.domain.product.ProductOption;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.exception.OptionNotFoundException;
import gift.product.infrastructure.persistence.entity.ProductOptionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductOptionRepositoryImpl implements ProductOptionRepository {
    private final JpaProductOptionRepository jpaProductOptionRepository;

    public ProductOptionRepositoryImpl(JpaProductOptionRepository jpaProductOptionRepository) {
        this.jpaProductOptionRepository = jpaProductOptionRepository;
    }

    @Override
    public void save(Long productId, ProductOption option) {
        jpaProductOptionRepository.save(ProductOptionEntity.fromDomain(productId, option));
    }

    @Override
    public List<ProductOption> findAllByProductId(Long productId) {
        return jpaProductOptionRepository
                .findAllByProductId(productId)
                .stream()
                .map(ProductOptionEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<ProductOption> findById(Long optionId) {
        return jpaProductOptionRepository
                .findById(optionId)
                .map(ProductOptionEntity::toDomain);
    }

    @Override
    public Long getProductIdByOptionId(Long optionId) {
        return jpaProductOptionRepository
                .findById(optionId)
                .map(ProductOptionEntity::getProductId)
                .orElseThrow(OptionNotFoundException::new);
    }

    @Override
    public Long countByProductId(Long productId) {
        return jpaProductOptionRepository.countByProductId(productId);
    }

    @Override
    public boolean hasOption(Long productId, String optionName) {
        return jpaProductOptionRepository.existsByProductIdAndName(productId, optionName);
    }

    @Override
    public boolean hasOption(Long optionId) {
        return jpaProductOptionRepository.existsById(optionId);
    }

    @Override
    public void deleteById(Long id) {
        jpaProductOptionRepository.deleteById(id);
    }
}
