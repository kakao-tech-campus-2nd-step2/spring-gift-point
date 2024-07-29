package gift.service;

import gift.domain.JwtToken;
import gift.entity.Member;
import gift.repository.MemberRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtToken jwtToken;

    public MemberService(MemberRepository memberRepository, JwtToken jwtToken) {
        this.memberRepository=memberRepository;
        this.jwtToken = jwtToken;
    }

    public Long register(Member member) {
        if(memberRepository.findByEmail(member.getEmail()).isPresent()){
            return -1L;
        }

        memberRepository.save(member);
        return member.getId();
    }

    public Optional<Member> login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent() && member.get().getPassword().equals(password)){
            return member;
        }
        return Optional.empty();
    }

    public Long getMemberId(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            return member.get().getId();
        }
        return -1L;
    }

    public String createToken(Member member) {
        return jwtToken.createToken(member);
    }

    public boolean validateToken(String token) {
        String email = jwtToken.tokenToEmail(token);
        return memberRepository.findByEmail(email).isPresent();
    }




}