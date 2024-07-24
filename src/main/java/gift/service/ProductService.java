package gift.service;

import gift.dto.ProductDTO;
import java.util.List;

public interface ProductService {

    List<ProductDTO> readAll();

    List<ProductDTO> readProduct(int pageNumber, int pageSize);

    void create(ProductDTO prod);

    void update(long id, ProductDTO prod);

    void delete(long id);

}
