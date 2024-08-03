package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface WishedProductRepository extends JpaRepository<WishedProduct, Long> {

    Page<WishedProduct> findByMember(Member member, Pageable pageable);

    @Transactional
    void deleteByMemberAndProduct(Member member, Product product);

    boolean existsByMemberAndProduct(Member member, Product product);
}
