package gift.domain.Member;

import gift.domain.Member.dto.MemberDTO;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.user.UserDuplicateException;
import gift.global.jwt.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final JpaMemberRepository memberRepository;

    public MemberService(JpaMemberRepository jpaMemberRepository) {
        this.memberRepository = jpaMemberRepository;
    }

    /**
     * 회원 가입
     */
    public void join(MemberDTO memberDTO) {
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new UserDuplicateException(memberDTO.getEmail());
        }

        Member member = memberDTO.toMember();
        memberRepository.save(member);
    }


    /**
     * 로그인, 성공 시 JWT 반환
     */
    public String login(MemberDTO memberDTO) {
        Member member = memberRepository.findByEmailAndPassword(memberDTO.getEmail(), memberDTO.getPassword())
            .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "입력 정보가 올바르지 않습니다."));

        // jwt 토큰 생성
        String jwt = JwtProvider.generateToken(member);

        return jwt;
    }
}
