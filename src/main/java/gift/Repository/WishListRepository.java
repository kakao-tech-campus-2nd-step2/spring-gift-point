package gift.Repository;

import gift.DTO.WishList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

  @Query("SELECT wl FROM WishList wl JOIN FETCH wl.product WHERE wl.product.id = :productId")
  List<WishList> findByProductId(@Param("productId") Long productId);

  Page<WishList> findAll(Pageable pageable);
}

