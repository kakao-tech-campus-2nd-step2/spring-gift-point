package gift.repository;

import gift.entity.Wishlist;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistJpaDao extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByMember_EmailAndProduct_Id(String email, Long productId);

    Page<Wishlist> findAllByMember_Email(String email, Pageable pageable);

    @Transactional
    void deleteByMember_EmailAndProduct_Id(String email, Long productId);
}
