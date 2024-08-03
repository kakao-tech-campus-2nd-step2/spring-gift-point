package gift.service;

import gift.common.exception.notFound.NotFoundException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.util.JwtUtil;
import gift.dto.PointRequest;
import gift.entity.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public PointService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public Long getPoints(String token) {
        String email = jwtUtil.extractEmail(token);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);

        return member.getPoints();
    }

    public Long chargePoints(String token, Long memberId, PointRequest pointRequest) {
        String email = jwtUtil.extractEmail(token);
        Member admin = memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        Long newPoints = member.getPoints() + pointRequest.getPoint();
        member.setPoints(newPoints);
        memberRepository.save(member);

        return newPoints;
    }

}
