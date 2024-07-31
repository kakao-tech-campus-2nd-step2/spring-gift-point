package gift.service;

import gift.domain.Order.OrderRequest;
import gift.domain.Order.OrderResponse;
import gift.entity.MemberEntity;
import gift.entity.OptionEntity;
import gift.entity.OrderEntity;
import gift.entity.WishEntity;
import gift.error.NotFoundException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;

    public OrderService(
        OrderRepository orderRepository,
        MemberRepository memberRepository,
        OptionRepository optionRepository,
        WishRepository wishRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
    }

    @Transactional
    public OrderResponse addOrder(Long memberId, OrderRequest request, Long optionId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Not found Member"));
        OptionEntity optionEntity = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundException("Not found Option"));

        //옵션엔티티에서 주문 수량 만큼 수량 차감 후 저장.
        optionEntity.subtractQuantity(request.quantity());
        optionRepository.save(optionEntity);
        //주문 저장
        OrderEntity orderEntity = orderRepository.save(
            new OrderEntity(
                    optionEntity,
                    request.quantity(),
                    request.message(),
                    LocalDateTime.now()
                )
        );

        //실제 위시리스트에서 제거
        List<WishEntity> wishListEntities = wishRepository.findByMemberEntity(memberEntity);
        wishListEntities.stream()
            .filter(wishListEntity -> wishListEntity.equalByProductEntity(optionEntity.getProductEntity()))
            .forEach(wishListEntity -> wishRepository.deleteById(wishListEntity.getId()));

        return OrderResponse.from(orderEntity);
    }
}
