package gift.service;

import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.util.KakaoApiUtil;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OptionFacadeService {

    private final OptionService optionService;
    private final ProductService productService;

    private final WishlistService wishlistService;

    private final KakaoApiUtil kakaoApiUtil;
    private final SnsMemberService snsMemberService;

    public OptionFacadeService(OptionService optionService, ProductService productService,
        WishlistService wishlistService, KakaoApiUtil kakaoApiUtil,
        SnsMemberService snsMemberService) {
        this.optionService = optionService;
        this.productService = productService;
        this.wishlistService = wishlistService;
        this.kakaoApiUtil = kakaoApiUtil;
        this.snsMemberService = snsMemberService;
    }


    public Product findProductById(Long id) {
        return productService.getProductById(id);
    }

    public void addOption(Option option) {
        optionService.addOption(option);
    }

    public void updateOption(Option option, Long id) {
        optionService.updateOption(option, id);
    }

    public void deleteOption(Long id) {
        optionService.deleteOption(id);
    }

    public OrderResponseDTO orderOption(OrderRequestDTO orderRequestDTO, String email) {
        Option option = optionService.getOptionById(orderRequestDTO.getOptionId());
        int quantity = orderRequestDTO.getQuantity();
        String message = orderRequestDTO.getMessage();

        //상품이 wishlist에 있을 시 위시리스트에서 삭제
        List<Wish> list = wishlistService.getWishlistByEmail(email);
        for (Wish wish : list) {
            if (wish.getProduct().equals(option.getProduct())) {
                wishlistService.deleteWishlist(wish.getProduct().getId(), email);
            }
        }
        //옵션 수량 감소
        optionService.subtractOption(option.getId(), quantity);

        //카카오톡 메시지 보내기
        sendMessage(message, snsMemberService.getOauthAccessTokenByEmail(email), option, quantity);

        return new OrderResponseDTO(1L, option.getId(), quantity, LocalDateTime.now(), message);
    }

    private void sendMessage(String text, String accessToken, Option option, int quantity) {
        String sb = text + "\n["
            + option.getProduct().getName() + "] 상품을 선물하셨습니다.\n"
            + "상품 옵션: [" + option.getName() + "]\n"
            + "상품 수량: " + quantity;
        kakaoApiUtil.SendOrderMessage(sb, accessToken);
    }


}
