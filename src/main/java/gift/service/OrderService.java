package gift.service;

import gift.domain.*;
import gift.dto.OrderItemRequest;
import gift.dto.OrderRequest;
import gift.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductService productService;
    private final WishRepository wishRepository;
    private final KakaoService kakaoService;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ProductService productService, WishRepository wishRepository, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.productService = productService;
        this.wishRepository = wishRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public void placeOrder(OrderRequest orderRequest, Long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        Order order = new Order(member, orderRequest.getRecipientMessage());

        for (OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productService.getProductById(itemRequest.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Invalid product ID: " + itemRequest.getProductId());
            }

            Option option = productService.getOptionById(itemRequest.getOptionId());
            if (!option.getProduct().getId().equals(product.getId())) {
                throw new IllegalArgumentException("Option does not belong to the specified product");
            }

            productService.subtractOptionQuantity(option.getProduct().getId(), option.getName(), itemRequest.getQuantity());

            OrderItem orderItem = new OrderItem(order, product, option, itemRequest.getQuantity());

            order.getOrderItems().add(orderItem);

            Wish wish = wishRepository.findByProductIdAndMemberId(option.getProduct().getId(), memberId);
            if (wish != null) {
                wishRepository.delete(wish);
            }
        }

        orderRepository.save(order);
        kakaoService.sendOrderMessage(memberId, order);
    }
}
