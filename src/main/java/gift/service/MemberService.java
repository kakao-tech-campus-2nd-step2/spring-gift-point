package gift.service;

import gift.dto.MemberDto;
import gift.exception.AuthenticationException;
import gift.exception.ValueAlreadyExistsException;
import gift.jwt.JwtTokenProvider;
import gift.model.member.Member;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public void registerNewMember(MemberDto memberDto) {
        Member member = new Member(memberDto.email(),memberDto.password(), memberDto.role());
        memberRepository.findByEmail(member.getEmail()).ifPresent(existingMember -> {
            throw new ValueAlreadyExistsException("Email already exists in Database");
        });

        memberRepository.save(member);
    }

    public String returnToken(MemberDto memberDto){
        Member member = new Member(memberDto.email(),memberDto.password(), memberDto.role());
        return jwtTokenProvider.generateToken(member);
    }

    public String loginMember(MemberDto memberDto) {
        Member member = new Member(memberDto.email(),memberDto.password(), memberDto.role());
        Member registeredMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new AuthenticationException("Member not exists in Database"));

        if (member.isPasswordNotEqual(registeredMember.getPassword())){
            throw new AuthenticationException("Incorrect password");
        }

        return jwtTokenProvider.generateToken(member);
    }

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
}
