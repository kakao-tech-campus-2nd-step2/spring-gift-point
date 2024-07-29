package gift.service;

import gift.component.KakaoApiComponent;
import gift.dto.kakao.KakaoMessageRequest;
import gift.dto.OrderRequest;
import gift.exception.NotFoundException;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final KakaoApiComponent kakaoApiComponent;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, KakaoApiComponent kakaoApiComponent) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.kakaoApiComponent = kakaoApiComponent;
    }

    public Order make(OrderRequest request) {
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new NotFoundException("해당 optionId의 옵션이 존재하지 않습니다."));
        Order order = new Order(
                option,
                request.quantity(),
                request.message()
        );
        return orderRepository.save(order);
    }

    public void sendOrderMessage(OrderRequest request, Member member) {
        KakaoMessageRequest kakaoMessageRequest = new KakaoMessageRequest(
                "text",
                request.message(),
                "https://developers.kakao.com/",
                "https://developers.kakao.com/"
        );
        kakaoApiComponent.sendMessage(member.getAccessToken(), kakaoMessageRequest);
    }
}
