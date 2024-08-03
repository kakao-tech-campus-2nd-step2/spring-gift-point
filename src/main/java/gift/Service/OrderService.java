package gift.Service;

import gift.DTO.RequestOrderDTO;
import gift.DTO.ResponseOrderDTO;
import gift.Event.EventObject.SendMessageToMeEvent;
import gift.Exception.OptionNotFoundException;
import gift.Exception.OrderNotFoundException;
import gift.Model.Entity.*;
import gift.Model.Value.AccessToken;
import gift.Model.Value.CashReceipt;
import gift.Repository.OptionRepository;
import gift.Repository.OrderRepository;
import gift.Repository.WishRepository;
import gift.Util.KakaoUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final KakaoUtil kakaoUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberService memberService;

    public OrderService(OptionService optionService, WishRepository wishRepository, OptionRepository optionRepository, OrderRepository orderRepository,
                        KakaoUtil kakaoUtil, ApplicationEventPublisher eventPublisher, MemberService memberService) {
        this.optionService = optionService;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoUtil = kakaoUtil;
        this.eventPublisher = eventPublisher;
        this.memberService = memberService;
    }

    @Transactional
    public ResponseOrderDTO createOrder(Member member, RequestOrderDTO requestOrderDTO) {
        Option option = optionRepository.findById(requestOrderDTO.optionId()).orElseThrow(()->
                new OptionNotFoundException("매칭되는 옵션이 없습니다"));

        optionService.subtractQuantity(option.getId(), requestOrderDTO.quantity());

        List<Wish> wishList = wishRepository.findWishListByMember(member);
        wishList.stream()
                .filter(it->it.getProduct().equals(option.getProduct()))
                .findFirst()
                .ifPresent(wish->wishRepository.deleteById(wish.getId()));

        int totalPrice = getToalPrice(requestOrderDTO.quantity(), option.getProduct());
        memberService.subtractPoint(member, totalPrice);

        CashReceipt cashReceipt = null;
        if (requestOrderDTO.phoneNumber() != null)
            cashReceipt = new CashReceipt(requestOrderDTO.phoneNumber());

        Order order = orderRepository.save(
                new Order(option, member, requestOrderDTO.quantity(), LocalDateTime.now(), requestOrderDTO.message(), cashReceipt)
        );

        Optional<AccessToken> accessToken = member.getAccessToken();
        if(accessToken.isPresent()){
            eventPublisher.publishEvent(new SendMessageToMeEvent(accessToken.get(), requestOrderDTO.message()));
        }

        return ResponseOrderDTO.of(order);
    }

    private int getToalPrice(int quantity, Product product) {
        int totalPrice = product.getPrice().getValue() * quantity;
        if (totalPrice >= 50000) //주문 금액이 5만원 이상인 경우 10프로 할인
            totalPrice *= 0.9;

        return totalPrice;
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderDTO> getOrders(Member member){
       return orderRepository.findAllByMember(member)
               .stream()
               .map(ResponseOrderDTO::of)
               .toList();
    }

    @Transactional
    public void deleteOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당하는 주문을 찾을 수 없습니다"));
        order.checkOrderBelongsToMember(member);
        order.getOption().addQuantity(order.getQuantity().getValue());
        int totalPrice = getToalPrice(order.getQuantity().getValue(), order.getOption().getProduct());
        memberService.refundPoints(member, totalPrice);
        orderRepository.deleteById(orderId);
    }
}
