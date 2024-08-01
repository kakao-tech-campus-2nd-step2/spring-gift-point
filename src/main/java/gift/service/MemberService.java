package gift.service;


import gift.dto.MemberDto;
import gift.entity.Member;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;

import gift.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {


    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public String registerMember(MemberDto memberDto) {
        Member newMember = new Member(memberDto.getEmail(), memberDto.getPassword());
        memberRepository.save(newMember);
        return jwtTokenProvider.createToken(memberDto.getEmail());
    }

    public String login(String email, String password) {

        Member member = getMember(email);

        // 비밀번호 검증
        if (member != null && password.equals(member.getPassword())) {
            return jwtTokenProvider.createToken(email);
        }
        throw new RuntimeException("Invalid email or password");
    }

    public Member getMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            return member;
        } else {
            throw new MemberNotFoundException("Member with email " + email + " not found");
        }
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
