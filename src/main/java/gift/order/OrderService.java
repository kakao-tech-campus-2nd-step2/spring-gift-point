package gift.order;

import gift.member.MemberService;
import gift.member.entity.Member;
import gift.option.OptionService;
import gift.option.entity.Option;
import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import gift.token.JwtProvider;
import gift.wishlist.WishlistService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public OrderService(
        OptionService optionService,
        MemberService memberService,
        WishlistService wishlistService,
        JwtProvider jwtProvider
    ) {
        this.optionService = optionService;
        this.memberService = memberService;
        this.wishlistService = wishlistService;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public CreateOrderResponseDTO createOrder(
        CreateOrderRequestDTO createOrderRequestDTO,
        String accessToken
    ) {
        Option option = optionService.subtract(
            createOrderRequestDTO.getOptionId(),
            createOrderRequestDTO.getQuantity()
        );

        Member member = memberService.getMember(
            jwtProvider.getMemberTokenDTOFromToken(accessToken).getEmail()
        );

        member.subtractPoint(
            (long) option.getProduct().getPrice() * createOrderRequestDTO.getQuantity()
        );

        if (Optional.ofNullable(createOrderRequestDTO.getPhoneNumber()).isPresent()) {
            System.out.println("현금 영수증 로직");
        }

        wishlistService.deleteWishlistIfExists(option.getProduct(), member);

        return new CreateOrderResponseDTO(
            option.getProduct().getId(),
            createOrderRequestDTO.getOptionId(),
            createOrderRequestDTO.getQuantity(),
            LocalDateTime.now(),
            createOrderRequestDTO.getMessage()
        );
    }

}
