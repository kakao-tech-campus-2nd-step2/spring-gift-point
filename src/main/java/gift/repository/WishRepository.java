package gift.repository;
import gift.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByMemberId(Long memberId);
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);
}
