package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Member;
import gift.Model.Entity.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeOrderRepository {
    private final Map<Long, Order> orderMap = new HashMap<>();
    private Long id = 1L;

    public Order save(Order order) {
        orderMap.put(id++, order);
        return order;
    }

    public List<Order> findAllByMember(Member member) {
        return orderMap.values()
                .stream()
                .filter(it -> it.getMember().equals(member))
                .toList();
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderMap.get(id));
    }

    public void deleteById(Long id) {
        orderMap.remove(id);
    }
}
