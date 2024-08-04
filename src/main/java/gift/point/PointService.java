package gift.point;

import gift.common.auth.LoginMemberDto;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.point.model.PointRequest;
import gift.point.model.PointResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    private final MemberRepository memberRepository;

    public PointService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public PointResponse getPoint(LoginMemberDto loginMemberDto) {
        Integer point = memberRepository.findById(loginMemberDto.getId()).orElseThrow().getPoint();
        return new PointResponse(point);
    }

    public Page<Member> getAllPoints(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Transactional
    public void addPoint(Long memberId, PointRequest pointRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.addPoint(pointRequest.point());
    }
}
