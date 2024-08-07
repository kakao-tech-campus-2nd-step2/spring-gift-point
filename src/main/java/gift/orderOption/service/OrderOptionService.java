package gift.orderOption.service;

import gift.orderOption.dto.KakaoTalkDto;
import gift.member.entity.Member;
import gift.member.service.PointService;
import gift.orderOption.dto.OrderOptionRequest;
import gift.orderOption.dto.OrderOptionResponse;
import gift.orderOption.entity.CashReceipt;
import gift.orderOption.entity.OrderOption;
import gift.orderOption.repository.CashReceiptRepository;
import gift.orderOption.repository.OrderOptionRepository;
import gift.product.entity.Option;
import gift.product.exception.NoSuchOptionException;
import gift.product.repository.OptionRepository;
import gift.product.service.OptionService;
import gift.wishedProduct.repository.WishedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderOptionService {

    private final PointService pointService;
    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final WishedProductRepository wishedProductRepository;
    private final CashReceiptRepository cashReceiptRepository;
    private final OrderOptionRepository orderOptionRepository;
    private final KakaoTalkService kakaoTalkService;

    private static final int POINT_ACCUMULATION_RATE = 10;

    @Autowired
    public OrderOptionService(PointService pointService, OptionService optionService,
        OptionRepository optionRepository, WishedProductRepository wishedProductRepository,
        CashReceiptRepository cashReceiptRepository, OrderOptionRepository orderOptionRepository,
        KakaoTalkService kakaoTalkService) {
        this.pointService = pointService;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.wishedProductRepository = wishedProductRepository;
        this.cashReceiptRepository = cashReceiptRepository;
        this.orderOptionRepository = orderOptionRepository;
        this.kakaoTalkService = kakaoTalkService;
    }

    @Transactional
    public OrderOptionResponse order(Member member, OrderOptionRequest orderOptionRequest) {
        // 포인트 차감
        pointService.subtractPoint(member, orderOptionRequest.point());

        // 옵션 수량 차감
        optionService.buyOption(orderOptionRequest.optionId(), orderOptionRequest.quantity());

        // 위시 리스트에서 해당 상품 삭제
        Option option = optionRepository.findById(orderOptionRequest.optionId())
            .orElseThrow(NoSuchOptionException::new);
        wishedProductRepository.deleteByMemberAndProduct(member, option.getProduct());

        // 포인트 적립
        pointService.addPoint(member, calculatePoint(option, orderOptionRequest));

        // 주문 내역 저장
        OrderOption orderOption = orderOptionRequest.toOrderOption(option, member);
        if (orderOptionRequest.hasCashReceipt()) {
            CashReceipt cashReceipt = orderOptionRequest.toCashReceipt();
            cashReceipt = cashReceiptRepository.save(cashReceipt);
            orderOption.setCashReceipt(cashReceipt);
        }
        orderOption = orderOptionRepository.save(orderOption);

        // 카카오톡 메시지 보내기
        sendTalk(orderOptionRequest, option, member);
        return orderOption.toOrderOptionResponse();
    }

    private int calculatePoint(Option option, OrderOptionRequest orderOptionRequest) {
        int price = option.getProduct().getPrice();
        int quantity = orderOptionRequest.quantity();
        int point = orderOptionRequest.point();
        return (price * quantity - point) / POINT_ACCUMULATION_RATE;
    }

    private void sendTalk(OrderOptionRequest orderOptionRequest, Option option, Member member) {
        KakaoTalkDto kakaoTalkDto = KakaoTalkDto.of(
            orderOptionRequest.message(),
            option.getProduct().getImageUrl(),
            "http://localhost:8080/api/products/" + option.getProduct().getId(),
            option.getProduct().getName(), option.getProduct().getCategory().getName(),
            option.getProduct().getPrice()
        );
        kakaoTalkService.sendTalk(member.toDto(), kakaoTalkDto);
    }
}
