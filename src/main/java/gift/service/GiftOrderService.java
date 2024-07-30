package gift.service;

import gift.dto.GiftOrderRequestDto;
import gift.dto.GiftOrderResponseDto;
import gift.entity.GiftOrder;
import gift.repository.GiftOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GiftOrderService {
    private GiftOrderRepository giftOrderRepository;
    private OptionService optionService;
    private WishService wishService;
    private MemberService memberService;

    public GiftOrderService(GiftOrderRepository giftOrderRepository, OptionService optionService, WishService wishService, MemberService memberService) {
        this.giftOrderRepository = giftOrderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.memberService = memberService;
    }

    public GiftOrderResponseDto placeOrderWithMessage(GiftOrderRequestDto giftOrderRequestDto, Long orderMemberId) throws IllegalAccessException {

        var option = optionService.getOptionById(giftOrderRequestDto.getOptionId());
        var member = memberService.getById(orderMemberId);

        GiftOrderResponseDto giftOrderResponseDto = GiftOrderResponseDto.fromEntity(giftOrderRepository.save(new GiftOrder(option, giftOrderRequestDto.getQuantity(), LocalDateTime.now(), giftOrderRequestDto.getMessage(), member)));

        optionService.subtract(option, giftOrderResponseDto.getQuantity());
        var wish = wishService.findByProductIdAndMemberId(option.getProduct().getId(), orderMemberId);

        if (wish != null) {
            wishService.delete(wish.getId());
        }

        return giftOrderResponseDto;
    }

}
