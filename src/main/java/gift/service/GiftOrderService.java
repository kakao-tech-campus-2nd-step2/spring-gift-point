package gift.service;

import gift.dto.GiftOrderRequestDto;
import gift.dto.GiftOrderResponseDto;
import gift.dto.MemberPointSubtractRequestDto;
import gift.entity.GiftOrder;
import gift.repository.GiftOrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GiftOrderService {
    private final GiftOrderRepository giftOrderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final MemberService memberService;

    public GiftOrderService(GiftOrderRepository giftOrderRepository, OptionService optionService, WishService wishService, MemberService memberService) {
        this.giftOrderRepository = giftOrderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.memberService = memberService;
    }

    @Transactional
    public GiftOrderResponseDto placeOrderWithMessage(GiftOrderRequestDto giftOrderRequestDto, Long orderMemberId) throws IllegalAccessException {

        var option = optionService.getOptionById(giftOrderRequestDto.getOptionId());
        var member = memberService.getById(orderMemberId);

        GiftOrder giftOrder = giftOrderRepository.save(new GiftOrder(option, giftOrderRequestDto.getQuantity(), LocalDateTime.now(), giftOrderRequestDto.getMessage(), member));
        Long productPrice = giftOrder.getProduct().getPrice();
        Long productCount = giftOrder.getQuantity();
        Long originalPrice = productPrice * productCount;
        Long finalPrice = originalPrice;

        if (originalPrice >= 50000) {
            finalPrice = originalPrice * 90 / 100;
        }

        GiftOrderResponseDto giftOrderResponseDto = new GiftOrderResponseDto(
                giftOrder.getId(),
                giftOrder.getOption().getId(),
                giftOrder.getQuantity(),
                giftOrder.getOrderDateTime(),
                giftOrder.getMessage(),
                originalPrice,
                finalPrice);

        MemberPointSubtractRequestDto memberPointSubtractRequestDto = new MemberPointSubtractRequestDto(finalPrice);
        memberService.updateMemberPoint(memberPointSubtractRequestDto, orderMemberId);

        optionService.subtract(option, giftOrderResponseDto.getQuantity());
        var wish = wishService.findByProductIdAndMemberId(option.getProduct().getId(), orderMemberId);

        if (wish != null) {
            wishService.delete(wish.getId());
        }

        return giftOrderResponseDto;
    }

}
