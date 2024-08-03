package gift.service;

import gift.dto.response.PointResponse;
import gift.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final MemberService memberService;

    public PointService(MemberService memberService) {
        this.memberService = memberService;
    }

    public PointResponse getMemberPoint(Long memberId) {
        Member member = memberService.getMember(memberId);
        return new PointResponse(member.getPoint());
    }
}
