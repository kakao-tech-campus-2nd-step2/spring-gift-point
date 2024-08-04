package gift.repository.product;

import gift.model.product.Product;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductMapRepository implements ProductRepository {

    private final Map<Long, Product> database = new ConcurrentHashMap<>();

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findAllOrderByPrice(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findByNameContaining(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Product> findByCategoryId(String searchValue, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(database.values());
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }

}
