package gift.repository;

import gift.dto.option.OptionResponseDTO;
import gift.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Integer> {
    @Query("select o from Option o where o.name = :option and o.product.id = :productId")
    Optional<Option> findByOption(int productId, String option);

    @Query("select o from Option o where o.product.name =:name and o.name = :option")
    Optional<Option> findByProductNameAndOption(String name, String option);

    @Modifying
    @Query("delete from Option o where o.product.id = :id")
    void deleteByProductId(int id);

    @Query("select new gift.dto.option.OptionResponseDTO(o.id,o.name,o.quantity,o.product.id) from Option o where o.product.id = :productId")
    List<OptionResponseDTO> findByProductId(int productId);
}
