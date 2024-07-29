package gift.core.domain.product;

import gift.core.PagedDto;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryService {

    PagedDto<ProductCategory> findAll(Pageable pageable);

}
