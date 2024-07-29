package gift.core.domain.product;

import gift.core.PagedDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductCategoryRepository {

    Optional<ProductCategory> findById(Long id);

    Optional<ProductCategory> findByName(String name);

    PagedDto<ProductCategory> findAll(Pageable pageable);

    ProductCategory save(ProductCategory category);

    void remove(Long id);

}
