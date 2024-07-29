package gift.order;

import gift.member.MemberService;
import gift.oauth.KakaoOauthService;
import gift.option.OptionService;
import gift.option.entity.Option;
import gift.order.dto.CreateOrderRequestDTO;
import gift.order.dto.CreateOrderResponseDTO;
import gift.wishlist.WishlistService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishlistService wishlistService;
    private final MemberService memberService;
    private final KakaoOauthService kakaoOauthService;

    public OrderService(
        OptionService optionService,
        MemberService memberService,
        WishlistService wishlistService,
        KakaoOauthService kakaoOauthService
    ) {
        this.optionService = optionService;
        this.memberService = memberService;
        this.wishlistService = wishlistService;
        this.kakaoOauthService = kakaoOauthService;
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
                kakaoOauthService.getEmailAndSubFromAccessToken(accessToken).getFirst()
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
