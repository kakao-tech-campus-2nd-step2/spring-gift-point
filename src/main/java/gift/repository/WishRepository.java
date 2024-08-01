package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMember(Member member);

    Page<Wish> findByMember(Member member, Pageable pageable);
    boolean existsByMemberAndProduct(Member member, Product product);
    void deleteByMemberAndProduct(Member member, Product product);
}
