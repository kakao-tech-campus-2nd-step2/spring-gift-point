package gift.category.repository;

import gift.category.domain.Category;
import gift.global.MyCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository extends MyCrudRepository<Category, Long> {
}
