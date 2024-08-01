package gift.domain.wish;

import gift.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findAllByMemberId(Long memberId);

    void deleteById(Long id);

    List<Wish> findAllByMember(Member member);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    // paging
    Page<Wish> findAllByMemberId(Long memberId, PageRequest pageRequest);

}
