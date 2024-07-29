package gift.domain.cartItem;

import gift.domain.Member.Member;
import gift.domain.product.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByMemberId(Long memberId);

    void deleteById(Long id);

    List<CartItem> findAllByMember(Member member);

    Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId);

    // paging
    Page<CartItem> findAllByMemberId(Long memberId, PageRequest pageRequest);

}
