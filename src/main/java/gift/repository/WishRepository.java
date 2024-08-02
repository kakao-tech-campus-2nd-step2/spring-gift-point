package gift.repository;

import gift.domain.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishRepository extends JpaRepository<Wish, Long> {

    void deleteByProductIdAndUserInfoId(Long productId, Long userId);

    Page<Wish> findByUserInfoId(Long userId, Pageable pageable);

    boolean existsByUserInfoIdAndProductId(Long userId, Long productId);

    Optional<Wish> findByUserInfoIdAndProductId(Long userId, Long productId);

    @Query("SELECT w FROM Wish w " +
        "WHERE w.id = :id and w.userInfo.email = :email")
    Optional<Wish> findWishByIdAndMemberEmail(@Param("id") Long id, @Param("email") String email);

}
