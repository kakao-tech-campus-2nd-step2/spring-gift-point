package gift.wishlist.dao;

import gift.wishlist.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishesRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findByMember_Id(Long memberId, Pageable pageable);

    Optional<Wish> findByMember_IdAndProduct_Id(Long memberId, Long productId);

}