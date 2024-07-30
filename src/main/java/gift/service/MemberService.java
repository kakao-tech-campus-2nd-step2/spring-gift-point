package gift.service;

import gift.domain.Member;
import gift.domain.Token;
import gift.entity.MemberEntity;
import gift.error.AlreadyExistsException;
import gift.error.ForbiddenException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
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
    public Token register(Member member) {
        memberRepository.findByEmail(member.getEmail())
            .ifPresent(m -> { throw new AlreadyExistsException("Already Exists Member"); });
        member.setPassword(PasswordUtil.encodePassword(member.getPassword()));
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setEmail(member.getEmail());
        memberEntity.setPassword(member.getPassword());
        MemberEntity loginMemberEntity = memberRepository.save(memberEntity);
        return new Token(jwtUtil.generateToken(MemberEntity.toDto(loginMemberEntity)));
    }

    @Transactional
    public Token login(Member member) {
        MemberEntity memberEntity = memberRepository.findByEmail(member.getEmail())
            .orElseThrow(() -> new ForbiddenException("Invalid email or Not Exists Member"));
        if (PasswordUtil.matches(member.getPassword(), memberEntity.getPassword())) {
            return new Token(jwtUtil.generateToken(MemberEntity.toDto(memberEntity)));
        }
        throw new ForbiddenException("Invalid password");
    }

    @Transactional
    public Token kakaoLogin(Long kakaoId, String kakaoToken) {
        String email = kakaoId + "@kakao.com";
        MemberEntity memberEntity = memberRepository.findByEmail(email)
            .orElseGet(() -> memberRepository.save(
                new MemberEntity(email,PasswordUtil.encodePassword("1111"), kakaoToken)
            ));
        return new Token(jwtUtil.generateToken(MemberEntity.toDto(memberEntity)));
    }

    public Optional<MemberEntity> getMemberEntityById(Long id) {
        return memberRepository.findById(id);
    }

}
