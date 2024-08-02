package gift.service;

import gift.domain.CashReceipt;
import gift.domain.Option;
import gift.domain.OrderOption;
import gift.dto.KakaoTalkRequest;
import gift.dto.member.MemberDto;
import gift.dto.orderOption.OrderOptionRequest;
import gift.dto.orderOption.OrderOptionResponse;
import gift.exception.NoSuchOptionException;
import gift.repository.CashReceiptRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderOptionRepository;
import gift.repository.WishedProductRepository;
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

        KakaoTalkRequest kakaoTalkRequest = KakaoTalkRequest.of(
            orderOptionRequest.message(),
            option.getProduct().getImageUrl(),
            "http://localhost:8080/api/products/" + option.getProduct().getId(),
            option.getProduct().getName(), option.getProduct().getCategory().getName(),
            option.getProduct().getPrice()
        );
        kakaoTalkService.sendTalk(memberDto, kakaoTalkRequest);
        return orderOption.toOrderOptionResponse();
    }
}
