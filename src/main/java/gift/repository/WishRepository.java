package gift.repository;


import gift.entity.Wish;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    public List<Wish> findByMemberId(Long memberId);

    public void deleteByMemberIdAndProductId(Long memberId, Long productId);

    public Page<Wish> findByMemberId(Long memberId, Pageable pageable);
}
