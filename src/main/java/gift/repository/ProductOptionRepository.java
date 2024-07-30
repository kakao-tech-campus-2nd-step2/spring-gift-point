package gift.repository;

import gift.entity.Option;
import gift.entity.OptionName;
import gift.entity.Product;
import gift.entity.ProductOption;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    boolean existsByProductAndOptionName(Product product, OptionName optionName);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select po from ProductOption po where po.id = :id")
    ProductOption findByIdWithLock(@Param("id") Long id);

    List<ProductOption> findByProduct(Product product);

    @Query("SELECT po FROM ProductOption po WHERE po.product = :product AND po.option = :option")
    Optional<ProductOption> findByProductAndOption(@Param("product") Product product, @Param("option") Option option);
}
