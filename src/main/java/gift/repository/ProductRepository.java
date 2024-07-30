package gift.repository;

import gift.dto.product.ResponseProductDTO;
import gift.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT new gift.dto.product.ResponseProductDTO(p.id, p.name , p.price , p.imageUrl) FROM Product p where p.category.id=:categoryId")
    Page<ResponseProductDTO> findByCategoryId(Pageable pageable, int categoryId);

    @Query("select p from Product p where p.name=:name and p.category.id = :categoryId")
    Optional<Product> findByNameAndCategory(String name, int categoryId);
}
