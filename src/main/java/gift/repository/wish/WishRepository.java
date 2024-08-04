package gift.repository.wish;

import gift.model.member.Member;
import gift.model.product.Product;
import gift.model.wish.Wish;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishRepository {

    Optional<Wish> findByMemberAndProduct(Member member, Product product);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    Wish save(Wish entity);

    Optional<Wish> findById(Long id);

    void deleteById(Long id);

    Page<Wish> findAllByMemberByIdDesc(Long memberId, Pageable pageable);


}
