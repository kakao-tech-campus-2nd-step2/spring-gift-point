package gift.repository;

import gift.model.GiftOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftOrderRepository extends JpaRepository<GiftOrder, Long> {

    List<GiftOrder> findAllByMemberId(Long memberId, Pageable pageable);

    void deleteAllByOptionId(Long optionId);

    void deleteAllByMemberId(Long memberId);
}
