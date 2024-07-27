package gift.wish.repository;

import gift.member.model.Member;
import gift.wish.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findById(Long wishId);

    @Query("SELECT w FROM Wish w JOIN w.product p ORDER BY p.price DESC")
    Page<Wish> findAllByOrderByProductPriceDesc(Pageable pageable);

    @Query("SELECT w FROM Wish w JOIN w.product p ORDER BY p.price ASC")
    Page<Wish> findAllByOrderByProductPriceAsc(Pageable pageable);
}
