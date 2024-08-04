package gift.service;

import gift.domain.MemberDomain.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberPointService {
    private MemberRepository memberRepository;

    public MemberPointService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Integer getPoint(String jwtId) {
        Member member = memberRepository.findById(jwtId).get();
        return member.getPoint();
    }
}
