package gift.order.infrastructure;

import gift.core.domain.order.Order;
import gift.core.domain.product.Product;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.ProductRepository;
import gift.core.domain.product.exception.ProductNotFoundException;
import gift.order.service.OrderPointStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultOrderPointStrategy implements OrderPointStrategy {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    @Value("${app.order.point-giving-ratio}") private Double pointGivingRatio;

    public DefaultOrderPointStrategy(
            ProductRepository productRepository,
            ProductOptionRepository productOptionRepository
    ) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Override
    public Long calculatePoint(Order order) {
        Long productId = productOptionRepository.getProductIdByOptionId(order.optionId());
        Product product = productRepository
                .findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        Long orderPrice = Long.valueOf(product.price()) * order.quantity();
        return (long) Math.ceil(orderPrice * pointGivingRatio);
    }
}
