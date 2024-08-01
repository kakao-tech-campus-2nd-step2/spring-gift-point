package gift.repository;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMember(Member member, Pageable pageable);
    Optional<Wish> findByMemberAndProduct(Member member, Product product);
    void deleteByProduct_Id(Long productId);
}
