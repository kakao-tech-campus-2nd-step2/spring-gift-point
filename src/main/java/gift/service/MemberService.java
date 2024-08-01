package gift.service;

import gift.domain.KakaoToken;
import gift.domain.Member.MemberRequest;
import gift.domain.Member.MemberResponse;
import gift.domain.Token;
import gift.entity.MemberEntity;
import gift.error.AlreadyExistsException;
import gift.error.ForbiddenException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import gift.util.LoginType;
import gift.util.PasswordUtil;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public Token register(MemberRequest request) {
        memberRepository.findByEmail(request.email())
            .ifPresent(m -> { throw new AlreadyExistsException("Already Exists Member"); });
        MemberEntity memberEntity = new MemberEntity(
            request.email(),
            PasswordUtil.encodePassword(request.password()),
            LoginType.DEFAULT
        );
        MemberEntity loginMemberEntity = memberRepository.save(memberEntity);
        return new Token(jwtUtil.generateToken(MemberResponse.from(loginMemberEntity)));
    }

    @Transactional
    public Token login(MemberRequest request) {
        MemberEntity memberEntity = memberRepository.findByEmail(request.email())
            .orElseThrow(() -> new ForbiddenException("Invalid email or Not Exists Member"));
        if (PasswordUtil.matches(request.password(), memberEntity.getPassword())) {
            return new Token(jwtUtil.generateToken(MemberResponse.from(memberEntity)));
        }
        throw new ForbiddenException("Invalid password");
    }

    @Transactional
    public Token kakaoLogin(Long kakaoId, KakaoToken kakaoToken) {
        String email = kakaoId + "@kakao.com";
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(
                new MemberEntity(
                    email,
                    PasswordUtil.encodePassword("1111"),
                    LoginType.KAKAO,
                    kakaoToken)
            ));
        return new Token(jwtUtil.generateToken(MemberResponse.from(memberEntity)));
    }

    public Optional<MemberEntity> getMemberEntityById(Long id) {
        return memberRepository.findById(id);
    }

}
