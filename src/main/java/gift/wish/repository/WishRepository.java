package gift.wish.repository;

import gift.member.model.Member;
import gift.wish.model.Wish;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findByMember(Member member);

    List<Wish> findByProductId(Long productId);

    int deleteByIdAndMember(Long wishId, Member member);

    Page<Wish> findAllByMember(Pageable pageable,Member member);
}
