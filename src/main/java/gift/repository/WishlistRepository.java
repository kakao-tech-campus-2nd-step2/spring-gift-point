package gift.repository;
import gift.model.Member;
import gift.model.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishList, Long> {
    Page<WishList> findByMember(Member member, Pageable pageable);

    //void deleteById(Long productId);
}
