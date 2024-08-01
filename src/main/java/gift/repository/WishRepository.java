package gift.repository;

import gift.model.Member;
import gift.model.Wish;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findByMember(Member member);

    List<Wish> findByProductId(Long productId);

    int deleteByIdAndMember(Long wishId, Member member);
}
