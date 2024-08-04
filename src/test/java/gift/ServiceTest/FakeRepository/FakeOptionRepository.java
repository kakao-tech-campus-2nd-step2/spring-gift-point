package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Option;
import gift.Model.Entity.Product;

import java.util.*;

public class FakeOptionRepository {
    private final Map<Long, Option> optionMap = new HashMap<>();
    private Long id = 1L;

    public Option save(Option option) {
        optionMap.put(id++, option);
        return option;
    }

    public List<Option> findByProduct(Product product) {
        return optionMap.values()
                .stream()
                .filter(it -> it.getProduct().equals(product))
                .toList();
    }

    public Optional<Option> findById(Long id) {
        return Optional.ofNullable(optionMap.get(id));
    }

    public void deleteById(Long id) {
        optionMap.remove(id);
    }
}
