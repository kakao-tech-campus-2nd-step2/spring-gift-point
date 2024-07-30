package gift.repository;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @EntityGraph(attributePaths = {"member", "product"})
    Page<Wishlist> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"member", "product"})
    Page<Wishlist> findByMember(Member member, Pageable pageable);

    void deleteByMemberAndProduct(Member member, Product product);

    void deleteByProduct(Product product);

    void deleteByProductIn(List<Product> products);
}