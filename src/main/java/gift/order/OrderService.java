package gift.order;

import gift.member.MemberService;
import gift.option.OptionService;
import gift.option.entity.Option;
import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import gift.token.JwtProvider;
import gift.wishlist.WishlistService;
import java.time.LocalDateTime;
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

        wishlistService.deleteWishlistIfExists(
            option.getProduct(),
            memberService.getMember(
                jwtProvider.getMemberTokenDTOFromToken(accessToken).getEmail()
            )
        );

        return new CreateOrderResponseDTO(
            option.getProduct().getId(),
            createOrderRequestDTO.getOptionId(),
            createOrderRequestDTO.getQuantity(),
            LocalDateTime.now(),
            createOrderRequestDTO.getMessage()
        );
    }

}
