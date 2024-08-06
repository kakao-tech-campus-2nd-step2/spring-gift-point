package gift.service;

import gift.domain.member.Member;
import gift.domain.member.MemberRequest;
import gift.domain.member.MemberType;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String register(MemberRequest memberRequest) {
        if (memberRepository.findByEmail(memberRequest.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다");
        }
        Member member = memberRepository.save(memberRequest.toMember());
        return jwtUtil.generateToken(member);
    }

    public String login(MemberRequest memberRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmailAndPassword(
            memberRequest.email(), memberRequest.password());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return jwtUtil.generateToken(member);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 일치하지 않습니다");
        }
    }

    public String kakaoLogin(Long kakaoId, String kakaoToken) {
        Optional<Member> memberOptional = memberRepository.findByKakaoId(kakaoId);
        Member member = memberOptional.orElseGet(
            () -> memberRepository.save(new Member(MemberType.KAKAO, kakaoId)));
        member.setKakaoToken(kakaoToken);
        memberRepository.save(member);
        return jwtUtil.generateToken(member);
    }
}
