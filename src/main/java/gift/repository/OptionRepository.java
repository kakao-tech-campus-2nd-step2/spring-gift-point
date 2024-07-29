package gift.repository;

import gift.dto.product.ProductWithOptionDTO;
import gift.entity.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    @Query("SELECT new gift.dto.product.ProductWithOptionDTO(p.id, p.name , p.price , p.imageUrl , o.option, p.category.name) FROM Product p join Option o ON p.id = o.product.id")
    Page<ProductWithOptionDTO> findAllWithOption(Pageable pageable);

    @Query("select o from Option o where o.option = :option")
    Optional<Option> findByOption(String option);

    @Query("select o from Option o where o.product.name =:name and o.option = :option")
    Optional<Option> findByProductNameAndOption(String name, String option);

    @Query("delete from Option o where o.product.id = :id")
    void deleteByProductId(int id);
}
