package gift.product.service.facade;

import gift.auth.service.KakaoOAuthService;
import gift.common.annotation.Facade;
import gift.member.service.MemberService;
import gift.product.service.KakaoMessageService;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.product.service.command.BuyProductCommand;
import gift.product.service.command.BuyProductMessageCommand;
import gift.product.service.command.ProductCommand;
import gift.product.service.command.ProductOptionCommand;
import gift.wish.persistence.WishRepository;
import gift.wish.service.WishService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Facade
public class ProductFacade {
    private static final Logger log = LoggerFactory.getLogger(ProductFacade.class);
    private final ProductService productService;
    private final MemberService memberService;
    private final WishService wishService;

    private final ProductOptionService productOptionService;
    private final KakaoOAuthService kakaoOAuthService;
    private final KakaoMessageService kakaoMessageService;
    private final WishRepository wishRepository;

    public ProductFacade(ProductService productService, ProductOptionService productOptionService,
                         KakaoOAuthService kakaoOAuthService, KakaoMessageService kakaoMessageService,
                         MemberService memberService, WishService wishService, WishRepository wishRepository) {
        this.productService = productService;
        this.productOptionService = productOptionService;
        this.kakaoOAuthService = kakaoOAuthService;
        this.kakaoMessageService = kakaoMessageService;
        this.memberService = memberService;
        this.wishService = wishService;
        this.wishRepository = wishRepository;
    }

    @Transactional
    public Long saveProduct(ProductCommand productCommand, List<ProductOptionCommand> productOptionCommands) {
        var productId = productService.saveProduct(productCommand);
        productOptionService.createProductOptions(productId, productOptionCommands);
        return productId;
    }

    public void purchaseProduct(BuyProductCommand buyProductCommand) {
        productOptionService.buyProduct(buyProductCommand.productId(), buyProductCommand.optionId(),
                buyProductCommand.quantity());
        wishService.deleteWishWithPurchase(buyProductCommand.productId(), buyProductCommand.memberId());

        var messageCommand = generateBuyProductMessageCommand(buyProductCommand.productId(),
                buyProductCommand.optionId(), buyProductCommand.memberId(), buyProductCommand.message(),
                buyProductCommand.quantity());

        kakaoMessageService.sendBuyProductMessage(messageCommand);
    }

    private BuyProductMessageCommand generateBuyProductMessageCommand(Long productId, Long optionId, Long memberId,
                                                                      String message, Integer quantity
    ) {
        var productInfo = productService.getProductDetails(productId);
        var optionInfo = productOptionService.getProductOptionInfo(productId, optionId);
        var username = memberService.getUsername(memberId);
        var accessToken = kakaoOAuthService.getAccessToken(username);

        return BuyProductMessageCommand.of(productInfo, optionInfo, accessToken, message, quantity);
    }
}
