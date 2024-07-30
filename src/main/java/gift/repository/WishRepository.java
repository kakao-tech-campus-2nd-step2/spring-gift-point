package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findAllByMember(Member member, Pageable pageable);

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    boolean existsByMemberAndProduct(Member member, Product product);
}
