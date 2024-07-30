package gift.application.product.service;

import gift.application.product.dto.OptionCommand;
import gift.application.product.dto.OptionModel;
import gift.application.product.dto.OrdersModel;
import gift.model.order.Orders;
import gift.repository.product.OptionRepository;
import gift.repository.product.OrdersRepository;
import gift.repository.product.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;

    public OrdersService(OrdersRepository ordersRepository, ProductRepository productRepository,
        OptionRepository optionRepository) {
        this.ordersRepository = ordersRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public void saveOrders(Long memberId, OptionModel.PurchaseInfo purchaseInfo) {
        // 구매 로직
        Orders orders = new Orders(purchaseInfo.productId(), memberId, purchaseInfo.optionId(),
            purchaseInfo.quantity(), purchaseInfo.totalPrice());
        ordersRepository.save(orders);
    }

    @Transactional(readOnly = true)
    public Page<OrdersModel.Info> getOrders(Long memberId, Pageable pageable) {
        Page<Orders> orders = ordersRepository.findAllByMemberId(memberId, pageable);
        return orders.map(order -> OrdersModel.Info.from(order,
            productRepository.findById(order.getProductId()).orElseThrow(),
            optionRepository.findById(order.getOptionId()).orElseThrow()));
    }
}
