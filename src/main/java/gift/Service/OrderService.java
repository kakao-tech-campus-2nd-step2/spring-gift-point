package gift.Service;

import gift.DTO.RequestOrderDTO;
import gift.DTO.ResponseOrderDTO;
import gift.Event.EventObject.SendMessageToMeEvent;
import gift.Exception.InvalidEditTypeException;
import gift.Exception.OptionNotFoundException;
import gift.Exception.OrderNotFoundException;
import gift.Model.Entity.*;
import gift.Model.Value.AccessToken;
import gift.Model.Value.Quantity;
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

    public OrderService(OptionService optionService, WishRepository wishRepository, OptionRepository optionRepository, OrderRepository orderRepository, KakaoUtil kakaoUtil, ApplicationEventPublisher eventPublisher) {
        this.optionService = optionService;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.kakaoUtil = kakaoUtil;
        this.eventPublisher = eventPublisher;
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

        Order order = orderRepository.save(
                new Order(option, member, requestOrderDTO.quantity(), LocalDateTime.now(), requestOrderDTO.message())
        );


        Optional<AccessToken> accessToken = member.getAccessToken();
        if(accessToken.isPresent()){
            eventPublisher.publishEvent(new SendMessageToMeEvent(accessToken.get(), requestOrderDTO.message()));
        }

        return ResponseOrderDTO.of(order);
    }

    @Transactional(readOnly = true)
    public List<ResponseOrderDTO> getOrders(Member member){
       return orderRepository.findAllByMember(member)
               .stream()
               .map(ResponseOrderDTO::of)
               .toList();
    }

    @Transactional
    public void editOrder(Member member, Long orderId, String editType, int deltaQuantity){
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new OrderNotFoundException("해당하는 주문을 찾을 수 없습니다"));
        order.checkOrderBelongsToMember(member);

        if (!(editType.equals("add") || editType.equals("subtract"))) {
            throw new InvalidEditTypeException("edit-type을 잘못적으셨습니다. add와 subtract 둘 중 하나로 적어주세요");
        }

        if (editType.equals("add")) {
            order.addQuantity(new Quantity(deltaQuantity));
        }

        if (editType.equals("subtract")) {
            order.subtractQuantity(new Quantity(deltaQuantity));
        }
    }

    @Transactional
    public void deleteOrder(Member member, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("해당하는 주문을 찾을 수 없습니다"));
        order.checkOrderBelongsToMember(member);
        order.getOption().addQuantity(order.getQuantity().getValue());
        orderRepository.deleteById(orderId);
    }
}
