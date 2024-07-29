package gift.wishlist;

import gift.member.Member;
import gift.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findAllByMember(Member member);

    Page<Wishlist> findAllByMember(Member member, Pageable pageable);

    Optional<Wishlist> findByProductAndMember(Product product, Member member);

}
