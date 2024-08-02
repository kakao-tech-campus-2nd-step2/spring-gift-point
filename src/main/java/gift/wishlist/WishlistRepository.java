package gift.wishlist;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishlist.entity.Wishlist;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Page<Wishlist> findAllByMember(Member member, Pageable pageable);

    Optional<Wishlist> findByProductAndMember(Product product, Member member);

}