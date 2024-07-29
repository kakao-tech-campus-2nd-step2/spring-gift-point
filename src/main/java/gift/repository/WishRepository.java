package gift.repository;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    String findAllByMemberIdQuery = """
                  SELECT new gift.model.Product(p.id, p.name, p.price, p.imageUrl)
                  FROM products p
                  JOIN wishes w ON p.id = w.product.id
                  WHERE w.member.id = :memberId
            """;

    @Query(findAllByMemberIdQuery)
    Page<Product> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    void deleteByProductAndMember(Product product, Member member);
}
