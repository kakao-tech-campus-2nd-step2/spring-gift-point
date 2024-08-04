package gift.wishedProduct.repository;

import gift.member.entity.Member;
import gift.product.entity.Product;
import gift.wishedProduct.entity.WishedProduct;
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
