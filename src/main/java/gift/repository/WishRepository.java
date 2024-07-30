package gift.repository;

import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMember(Member member);
    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    void deleteByMemberAndProduct(Member member, Product product);
    void deleteByOptionId(Long optionId);
    void deleteByOption(Option option);
    boolean existsByMemberAndProductAndOption(Member member, Product product, Option option);
}