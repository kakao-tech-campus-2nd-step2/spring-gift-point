package gift.Repository;

import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findWishListByMember(Member member);

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    Page<Wish> findByMember(Member member, Pageable pageable);
}
