package gift.service;

import gift.domain.Option;
import gift.dto.KakaoTalkRequest;
import gift.dto.KakaoTalkResponse;
import gift.dto.member.MemberDto;
import gift.dto.OrderOptionDTO;
import gift.exception.NoSuchOptionException;
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
    private final OrderOptionRepository orderOptionRepository;
    private final KakaoTalkService kakaoTalkService;

    @Autowired
    public OrderOptionService(OptionService optionService, OptionRepository optionRepository,
        WishedProductRepository wishedProductRepository, OrderOptionRepository orderOptionRepository,
        KakaoTalkService kakaoTalkService) {
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.wishedProductRepository = wishedProductRepository;
        this.orderOptionRepository = orderOptionRepository;
        this.kakaoTalkService = kakaoTalkService;
    }

    @Transactional
    public KakaoTalkResponse order(MemberDto memberDto, OrderOptionDTO orderOptionDTO) {
        optionService.buyOption(orderOptionDTO.optionId(), orderOptionDTO.quantity());
        Option option = optionRepository.findById(orderOptionDTO.optionId())
            .orElseThrow(NoSuchOptionException::new);
        wishedProductRepository.deleteAllByMemberAndProduct(memberDto.toEntity(), option.getProduct());
        orderOptionRepository.save(orderOptionDTO.toEntity(option, memberDto.toEntity()));

        KakaoTalkRequest kakaoTalkRequest = KakaoTalkRequest.of(orderOptionDTO.message(),
            option.getProduct().getImageUrl(),
            "http://localhost:8080/api/products/" + option.getProduct().getId(),
            option.getProduct().getName(), option.getProduct().getCategory().getName(),
            option.getProduct().getPrice());
        return kakaoTalkService.sendTalk(memberDto, kakaoTalkRequest);
    }
}
