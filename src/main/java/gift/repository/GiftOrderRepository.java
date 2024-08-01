package gift.repository;

import gift.model.GiftOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {

    Page<GiftOrder> findAllByMemberId(Long memberId, Pageable pageable);

    void deleteAllByOptionId(Long optionId);

    void deleteAllByMemberId(Long memberId);
}
