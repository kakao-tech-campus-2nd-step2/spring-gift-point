package gift.domain.wishlist.repository;

import gift.domain.member.entity.Member;
import gift.domain.product.entity.Product;
import gift.domain.wishlist.entity.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findAllByMember(Member member, Pageable pageable);

    Optional<Wish> findByProductAndMember(Product product, Member member);

    boolean existsByProductAndMember(Product product, Member member);
}

