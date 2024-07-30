package gift.wishes;


import gift.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findByMemberId(Long memberId);

    void deleteByIdAndMember(Long id, Member member);

    Optional<Wish> findByIdAndMember(Long id, Member member);
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

    Optional<Wish> findByProductIdAndMemberId(Long productId, Long memberId);
}
