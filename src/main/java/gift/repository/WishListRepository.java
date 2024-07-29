package gift.repository;

import gift.dto.product.ShowProductDTO;
import gift.entity.Product;
import gift.entity.User;
import gift.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {
    @Query("SELECT new gift.dto.product.ShowProductDTO(p.id, p.name, p.price, p.imageUrl,p.category.name) " +
            "FROM Product p join WishList w  on p.id = w.product.id where w.user.id = :userId")
    Page<ShowProductDTO> findByUserId(@Param("userId") int tokenUserId, Pageable pageable);

    @Query("select u from WishList u where u.product=:product and u.user=:user")
    Optional<WishList> findByUserAndProduct(User user, Product product);
}
