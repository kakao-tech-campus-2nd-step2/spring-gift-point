package gift.service;

import gift.dto.member.MemberAuthResponse;
import gift.dto.member.MemberEditResponse;
import gift.dto.option.OptionResponse;
import gift.dto.order.OrderRequest;
import gift.dto.order.OrderResponse;
import gift.model.Option;
import gift.model.OrderDetail;
import gift.model.Product;
import gift.model.RegisterType;
import gift.repository.OrderRepository;
import gift.service.oauth.KakaoOAuthService;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final KakaoOAuthService kakaoOAuthService;
    private final WishService wishService;
    private final MemberService memberService;

    public OrderService(
        OrderRepository orderRepository,
        OptionService optionService,
        KakaoOAuthService kakaoOAuthService,
        WishService wishService,
        MemberService memberService
    ) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.kakaoOAuthService = kakaoOAuthService;
        this.wishService = wishService;
        this.memberService = memberService;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, Long memberId) {
        OptionResponse optionResponse = optionService.getOptionById(orderRequest.optionId());

        optionService.subtractOptionQuantity(optionResponse.productId(), optionResponse.id(), orderRequest.quantity());
        Option option = optionService.convertToEntity(optionResponse);

        OrderDetail orderDetail = new OrderDetail(option, orderRequest.quantity(), orderRequest.message(), LocalDateTime.now());
        OrderDetail savedOrderDetail = orderRepository.save(orderDetail);

        MemberEditResponse memberEditResponse = memberService.getMemberById(memberId);
        if (memberEditResponse.registerType() == RegisterType.KAKAO) {
            sendKakaoMessage(savedOrderDetail, memberId);
        }

        wishService.deleteWishByProductIdAndMemberId(option.getProduct().getId(), memberId);

        return new OrderResponse(
            savedOrderDetail.getId(),
            option.getId(),
            orderDetail.getQuantity(),
            orderDetail.getOrderDateTime(),
            orderDetail.getMessage()
        );
    }

    private void sendKakaoMessage(OrderDetail orderDetail, Long memberId) {
        String message = createKakaoMessage(orderDetail);
        kakaoOAuthService.sendMessage(memberId, message);
    }

    private String createKakaoMessage(OrderDetail orderDetail) {
        Option option = orderDetail.getOption();
        Product product = option.getProduct();

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        String priceFormatted = numberFormat.format(product.getPrice());
        String quantityFormatted = numberFormat.format(orderDetail.getQuantity());
        String totalFormatted = numberFormat.format((long) product.getPrice() * orderDetail.getQuantity());

        String template = """
            {
                "object_type": "feed",
                "content": {
                    "title": "주문 완료 | 옵션: [%s] %s",
                    "description": "주문 메시지: %s",
                    "image_url": "https://picsum.photos/400/400",
                    "link": {
                        "web_url": "http://www.daum.net",
                        "mobile_web_url": "http://m.daum.net"
                    }
                },
                "item_content": {
                    "profile_text": "충남대 BE 이경빈",
                    "profile_image_url" :"https://avatars.githubusercontent.com/u/109949453?v=4",
                    "title_image_url": "%s",
                    "title_image_text": "%s",
                    "title_image_category": "%s",
                    "items": [
                        {
                            "item": "가격",
                            "item_op": "%s원"
                        },
                        {
                            "item": "수량",
                            "item_op": "%s개"
                        }
                    ],
                    "sum": "Total",
                    "sum_op": "%s원"
                },
                "social": {
                    "like_count": 100,
                    "comment_count": 200,
                    "shared_count": 300,
                    "view_count": 400,
                    "subscriber_count": 500
                }
            }
            """;

        return String.format(template,
            option.getId().toString(), option.getName(), orderDetail.getMessage(),
            product.getImageUrl(), product.getName(), product.getCategoryName(),
            priceFormatted, quantityFormatted, totalFormatted
        );
    }
}
