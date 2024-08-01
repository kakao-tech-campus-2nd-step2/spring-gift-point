package gift.domain.service;

import gift.domain.dto.request.OrderRequest;
import gift.domain.dto.response.OrderResponse;
import gift.domain.entity.Member;
import gift.domain.entity.Option;
import gift.domain.entity.Order;
import gift.domain.repository.OrderRepository;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final OauthService oauthService;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishService wishService,
        OauthService oauthService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.oauthService = oauthService;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(Member member) {
        return member.getOrders().stream()
            .map(OrderResponse::of)
            .toList();
    }

    @Transactional
    public OrderResponse createOrder(Member member, OrderRequest request) {
        Option option = optionService.getOptionById(request.optionId());
        Long productId = option.getProduct().getId();

        //해당 옵션 수량을 감소
        optionService.subtractQuantity(option, request.quantity());

        //멤버의 위시리스트를 불러온뒤 옵션에 해당하는 상품이 존재하면 그 상품을 삭제함
        //TODO: 위시리스트에서 상품 옵션에 대해 위시 수량을 지정하고, 그 수량을 감소시킬 필요가 있음
        wishService.getWishlist(member).stream()
            .filter(w -> w.product().id().equals(productId))
            .findAny()
            .ifPresent(w -> wishService.deleteWishlist(member, w.id()));

        //주문 저장
        Order order = orderRepository.save(request.toEntity(member, option));

        //카카오 멤버라면 카카오톡 메시지 전송
        if (member.getUserType() == Type.KAKAO) {
            oauthService.sendOrderInfoKakaoTalkSelfMessage(member.getKakaoOauthMember().getAccessToken(), order);
        }

        return OrderResponse.of(order);
    }
}
