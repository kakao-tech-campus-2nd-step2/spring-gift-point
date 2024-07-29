package gift.repository;

import gift.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public  interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    Page<Product> findAllById(@Param("ids") List<Long> productsId, Pageable pageable);

    Optional<Product> findById(Long productId);
}
