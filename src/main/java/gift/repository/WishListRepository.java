package gift.repository;

import gift.entity.Product;
import gift.entity.User;
import gift.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    @Query("select u from WishList u where u.product=:product and u.user=:user")
    Optional<WishList> findByUserAndProduct(User user, Product product);

    Page<WishList> findAllByUserId(int id, Pageable pageable);
}
