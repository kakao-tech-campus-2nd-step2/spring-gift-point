package gift.order.infrastructure.persistence;

import gift.core.PagedDto;
import gift.core.domain.order.Order;
import gift.core.domain.order.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        return jpaOrderRepository.save(OrderEntity.fromDomain(order)).toDomain();
    }

    @Override
    public PagedDto<Order> findAll(Pageable pageable) {
        Page<Order> orders = jpaOrderRepository
                .findAll(pageable)
                .map(OrderEntity::toDomain);
        return new PagedDto<>(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.getContent()
        );
    }
}
