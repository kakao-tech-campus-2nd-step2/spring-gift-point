package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Category;
import gift.Model.Entity.Product;

import java.util.*;

public class FakeProductRepository {
    private final Map<Long, Product> productMap = new HashMap<>();
    private Long id = 1L;

    public Product save(Product product) {
        productMap.put(id++, product);
        return product;
    }

    public List<Product> findAll(Category category) {
        return new ArrayList<>(productMap.values());
    }

    public List<Product> findByCategory(Category category) {
        return productMap.values()
                .stream()
                .filter(it -> it.getCategory().equals(category))
                .toList();
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    public void deleteById(Long id) {
        productMap.remove(id);
    }
}
