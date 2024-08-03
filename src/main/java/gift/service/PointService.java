package gift.service;

import gift.dto.request.OrderRequest;
import gift.dto.response.PointResponse;
import gift.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final MemberService memberService;
    private final OptionService optionService;

    public PointService(MemberService memberService, OptionService optionService) {
        this.memberService = memberService;
        this.optionService = optionService;
    }

    public PointResponse getMemberPoint(Long memberId) {
        Member member = memberService.getMember(memberId);
        return new PointResponse(member.getPointBalance());
    }

    public void subtractPoint(Long memberId, OrderRequest orderRequest) {
        int productPrice = optionService.getProductPrice(orderRequest.optionId());
        int totalPrice = productPrice * orderRequest.quantity();

        memberService.getMember(memberId)
                .subtractPoint(totalPrice);
    }
}
