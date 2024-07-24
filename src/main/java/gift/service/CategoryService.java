package gift.service;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import java.util.List;

public interface CategoryService {
    List<CategoryResponse> readAll();

    void create( CategoryRequest categoryRequest);

    void update(long id, CategoryRequest categoryRequest);

    void delete(long id);
}
