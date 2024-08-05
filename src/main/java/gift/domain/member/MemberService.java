package gift.domain.member;

import gift.domain.member.dto.request.MemberRequest;
import gift.domain.member.dto.response.MemberResponse;
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
    public MemberResponse join(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.email())) {
            throw new UserDuplicateException(memberRequest.email());
        }

        Member member = memberRequest.toMember();
        Member savedMember = memberRepository.save(member);
        String jwt = JwtProvider.generateToken(savedMember);

        return new MemberResponse(member.getEmail(), jwt);
    }


        /**
         * 로그인, 성공 시 JWT 반환
         */
        public MemberResponse login(MemberRequest memberRequest) {
            Member member = memberRepository.findByEmailAndPassword(memberRequest.email(), memberRequest.password())
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST, "입력 정보가 올바르지 않습니다."));

            String jwt = JwtProvider.generateToken(member);
            return new MemberResponse(member.getEmail(), jwt);
        }
}
