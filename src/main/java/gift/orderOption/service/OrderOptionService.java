package gift.orderOption.service;

import gift.orderOption.entity.CashReceipt;
import gift.product.entity.Option;
import gift.orderOption.entity.OrderOption;
import gift.kakao.dto.KakaoTalkDto;
import gift.member.dto.MemberDto;
import gift.orderOption.dto.OrderOptionRequest;
import gift.orderOption.dto.OrderOptionResponse;
import gift.product.exception.NoSuchOptionException;
import gift.product.service.OptionService;
import gift.orderOption.repository.CashReceiptRepository;
import gift.product.repository.OptionRepository;
import gift.orderOption.repository.OrderOptionRepository;
import gift.wishedProduct.repository.WishedProductRepository;
import gift.kakao.service.KakaoTalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderOptionService {

    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final WishedProductRepository wishedProductRepository;
    private final CashReceiptRepository cashReceiptRepository;
    private final OrderOptionRepository orderOptionRepository;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OrderOptionService(OptionService optionService, OptionRepository optionRepository,
        WishedProductRepository wishedProductRepository, CashReceiptRepository cashReceiptRepository,
        OrderOptionRepository orderOptionRepository, KakaoTalkService kakaoTalkService) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.wishedProductRepository = wishedProductRepository;
        this.cashReceiptRepository = cashReceiptRepository;
        this.orderOptionRepository = orderOptionRepository;
        this.kakaoTalkService = kakaoTalkService;
    }

    @Transactional
    public OrderOptionResponse order(MemberDto memberDto, OrderOptionRequest orderOptionRequest) {
        optionService.buyOption(orderOptionRequest.optionId(), orderOptionRequest.quantity());
        Option option = optionRepository.findById(orderOptionRequest.optionId())
            .orElseThrow(NoSuchOptionException::new);
        wishedProductRepository.deleteByMemberAndProduct(memberDto.toEntity(), option.getProduct());
        OrderOption orderOption = orderOptionRequest.toOrderOption(option, memberDto.toEntity());
        if(orderOptionRequest.hasCashReceipt()) {
            CashReceipt cashReceipt = orderOptionRequest.toCashReceipt();
            cashReceipt = cashReceiptRepository.save(cashReceipt);
            orderOption.setCashReceipt(cashReceipt);
        }
        orderOption = orderOptionRepository.save(orderOption);

        KakaoTalkDto kakaoTalkDto = KakaoTalkDto.of(
            orderOptionRequest.message(),
            option.getProduct().getImageUrl(),
            "http://localhost:8080/api/products/" + option.getProduct().getId(),
            option.getProduct().getName(), option.getProduct().getCategory().getName(),
            option.getProduct().getPrice()
        );
        kakaoTalkService.sendTalk(memberDto, kakaoTalkDto);
        return orderOption.toOrderOptionResponse();
    }
}
