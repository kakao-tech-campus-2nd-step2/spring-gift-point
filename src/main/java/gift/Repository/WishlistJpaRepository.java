package gift.Repository;

import gift.Entity.Member;
import gift.Entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByMemberId(long memberId);

    Page<Wishlist> findByMember(Member member, Pageable pageable);

    void deleteByMemberId(long memberId);

}
