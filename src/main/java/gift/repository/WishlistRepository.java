package gift.repository;

import gift.model.Member;
import gift.model.Option;
import gift.model.Wishlist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Page<Wishlist> findByMember(Member member, Pageable pageable);

    void deleteByOptionIn(List<Option> options);

    void deleteByOptionId(Long optionId);
}