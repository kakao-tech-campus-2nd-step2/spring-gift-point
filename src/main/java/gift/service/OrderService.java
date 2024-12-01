package gift.service;

import gift.dto.OrderRequestDto;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.vo.Member;
import gift.vo.Option;
import gift.vo.Order;
import gift.vo.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishlistService wishlistService, MemberService memberService, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    public Option getOptionByOptionId(Long optionId) {
        return optionService.getOption(optionId);
    }

    public Member getMemberByMemberId(Long memberId) {
        return memberService.getMemberById(memberId);
    }

    private void removeWish(Long wishId) {
        wishlistService.deleteWishProduct(wishId);
    }

    private void checkWishAndRemove(Member member, Option option) {
        Product product = option.getProduct();
        Long foundWishId = wishlistService.hasFindWishByMemberAndProduct(member, product);
        if (foundWishId != null) {
            removeWish(foundWishId);
        }
    }

    @Transactional
    public Order createOrder(Long memberId, OrderRequestDto orderRequestDto) {
        Member member = getMemberByMemberId(memberId);
        member.subtractPoint(orderRequestDto.usedPoint()); // 포인트 차감
        optionService.subtractOptionQuantity(orderRequestDto.optionId(), orderRequestDto.quantity());
        Order savedOrder = orderRepository.save(orderRequestDto.toOrder(memberId));

        Option option = getOptionByOptionId(savedOrder.getOptionId());
        checkWishAndRemove(member, option);

        return savedOrder;
    }

}
