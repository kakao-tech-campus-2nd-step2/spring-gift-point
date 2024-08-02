package gift.point;

import gift.common.auth.LoginMemberDto;
import gift.member.MemberRepository;
import org.springframework.stereotype.Service;

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
}
