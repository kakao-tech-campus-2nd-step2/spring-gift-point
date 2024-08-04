package gift.service;

import gift.common.exception.notFound.NotFoundException;
import gift.common.exception.unauthorized.TokenErrorException;
import gift.common.util.JwtUtil;
import gift.dto.PointRequest;
import gift.entity.Member;
import gift.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final MemberRepository memberRepository;

    public PointService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long getPoints(String token) {
        String email = JwtUtil.extractEmail(token);
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(TokenErrorException::new);

        return member.getPoints();
    }

    @Transactional
    public Long chargePoints(String token, Long memberId, PointRequest pointRequest) {
        String email = JwtUtil.extractEmail(token);
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
