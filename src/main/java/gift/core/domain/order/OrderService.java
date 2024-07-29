package gift.core.domain.order;

public interface OrderService {

    Order orderProduct(Order order, String gatewayAccessToken);

}
