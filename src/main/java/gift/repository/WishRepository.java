package gift.repository;

import gift.entity.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    List<Wish> findByMemberId(Long memberId);

    void deleteByOptionId(Long optionId);

}
