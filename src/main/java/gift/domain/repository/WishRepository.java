package gift.domain.repository;

import gift.domain.entity.Member;
import gift.domain.entity.Product;
import gift.domain.entity.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    List<Wish> findWishesByMember(Member member);

    Optional<Wish> findWishByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
}
