package gift.wishes.infrastructure.persistence;

import gift.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaWishRepository extends JpaRepository<WishEntity, Long> {

    boolean existsByUser_IdAndProduct_Id(Long userId, Long productId);

    void deleteByUser_IdAndProduct_Id(Long userId, Long productId);

    List<WishEntity> findAllByUser(UserEntity user);

    Page<WishEntity> findAllByUser(UserEntity user, Pageable pageable);

}
