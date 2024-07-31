package gift.wishes.infrastructure.persistence;

import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.product.exception.ProductNotFoundException;
import gift.core.domain.user.User;
import gift.core.domain.user.exception.UserNotFoundException;
import gift.core.domain.wishes.WishesRepository;
import gift.product.infrastructure.persistence.repository.JpaProductRepository;
import gift.product.infrastructure.persistence.entity.ProductEntity;
import gift.user.infrastructure.persistence.repository.JpaUserRepository;
import gift.user.infrastructure.persistence.entity.UserEntity;
import gift.wishes.service.WishDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class WishesRepositoryImpl implements WishesRepository {
    private final JpaWishRepository jpaWishRepository;
    private final JpaProductRepository jpaProductRepository;
    private final JpaUserRepository jpaUserRepository;

    @Autowired
    public WishesRepositoryImpl(
            JpaWishRepository jpaWishRepository,
            JpaProductRepository jpaProductRepository,
            JpaUserRepository jpaUserRepository
    ) {
        this.jpaWishRepository = jpaWishRepository;
        this.jpaProductRepository = jpaProductRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void saveWish(Long userId, Long productId) {
        UserEntity user = jpaUserRepository
                .findById(userId)
                .orElseThrow(UserNotFoundException::new);
        ProductEntity product = jpaProductRepository
                .findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        jpaWishRepository.save(new WishEntity(user, product, 1L));
    }

    @Override
    public void removeWish(Long userId, Long productId) {
        jpaWishRepository.deleteByUser_IdAndProduct_Id(userId, productId);
    }

    @Override
    public boolean exists(Long userId, Long productId) {
        return jpaWishRepository.existsByUser_IdAndProduct_Id(userId, productId);
    }

    @Override
    public List<WishDto> getWishlistOfUser(User user) {
        return jpaWishRepository.findAllByUser(UserEntity.fromDomain(user))
                .stream()
                .map(WishEntity::toDto)
                .toList();
    }

    @Override
    public PagedDto<WishDto> getWishlistOfUser(User user, Pageable pageable) {
        Page<WishDto> pagedProducts = jpaWishRepository
                .findAllByUser(UserEntity.fromDomain(user), pageable)
                .map((WishEntity::toDto));
        return new PagedDto<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pagedProducts.getTotalElements(),
                pagedProducts.getTotalPages(),
                pagedProducts.getContent()
        );
    }
}