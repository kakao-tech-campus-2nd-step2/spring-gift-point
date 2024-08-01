package gift.repository;

import gift.domain.ProductOption;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findAllByProductId(Long productId);

    boolean existsByNameAndProductId(String name, Long productId);

    /**
     * 해당 상품 내에서 현재 옵션을 제외하고 이름이 같은 옵션이 있는지 확인
     * @param id 현재 상품 옵션 아이디
     * @param productId 상품 아이디
     * @param name 상품 옵션 이름
     * @return 현재 옵션을 제외한 중복된 상품 옵션 아이디
     */
    @Query("SELECT po.id"
        + " FROM ProductOption po"
        + " WHERE po.id != :id AND po.productId = :productId AND po.name = :name"
        )
    Optional<Long> findDuplicatedProductOption(Long id, Long productId, String name);

    void deleteAllByProductId(Long productId);
}
