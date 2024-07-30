package gift.product.infra;

import gift.product.domain.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListJpaRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserId(Long userId);


    Page<WishList> findByUserId(Long userId, Pageable pageable);

}
