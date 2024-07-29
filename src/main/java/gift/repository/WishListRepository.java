package gift.repository;

import gift.model.Product;
import gift.model.User;
import gift.model.WishList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUser(User user);
    Optional<WishList> findByUserAndProduct(User user, Product product);
    Page<WishList> findByUser(User user, Pageable pageable);  // 추가
}