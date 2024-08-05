package gift.bill.service;

import gift.auth.service.KakaoOAuthService;
import gift.bill.service.command.BillFacadePurchaseCommand;
import gift.bill.service.dto.BillInfo;
import gift.common.annotation.Facade;
import gift.member.service.MemberService;
import gift.product.service.KakaoMessageService;
import gift.product.service.ProductOptionService;
import gift.product.service.ProductService;
import gift.product.service.command.BuyProductMessageCommand;
import gift.wish.service.WishService;

@Facade
public class BillFacadeService {
    private final BillService billService;
    private final ProductService productService;
    private final MemberService memberService;
    private final WishService wishService;

    private final ProductOptionService productOptionService;
    private final KakaoOAuthService kakaoOAuthService;
    private final KakaoMessageService kakaoMessageService;

    public BillFacadeService(ProductService productService, MemberService memberService, WishService wishService,
                             BillService billService,
                             ProductOptionService productOptionService, KakaoOAuthService kakaoOAuthService,
                             KakaoMessageService kakaoMessageService) {
        this.productService = productService;
        this.memberService = memberService;
        this.wishService = wishService;
        this.billService = billService;
        this.productOptionService = productOptionService;
        this.kakaoOAuthService = kakaoOAuthService;
        this.kakaoMessageService = kakaoMessageService;
    }

    public BillInfo purchaseProduct(BillFacadePurchaseCommand billFacadePurchaseCommand) {
        var billInfo = billService.purchaseProduct(billFacadePurchaseCommand.toBillCommand());
        wishService.deleteWishWithPurchase(billInfo.productId(), billFacadePurchaseCommand.memberId());

        sendMessage(billFacadePurchaseCommand, billInfo);

        return billInfo;
    }

    private void sendMessage(BillFacadePurchaseCommand billFacadePurchaseCommand, BillInfo billInfo) {
        var messageCommand = generateBuyProductMessageCommand(billInfo.productId(),
                billFacadePurchaseCommand.optionId(), billFacadePurchaseCommand.memberId(),
                billFacadePurchaseCommand.message(),
                billFacadePurchaseCommand.quantity());

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
