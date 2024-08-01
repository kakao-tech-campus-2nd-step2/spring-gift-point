package gift.repository;

import gift.model.Wish;
import gift.model.Member;
import gift.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMemberId(Long memberId);
    void deleteByIdAndMemberId(Long id, Long memberId);
    void deleteByMemberAndProduct(Member member, Product product);
}
