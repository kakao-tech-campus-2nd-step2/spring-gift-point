package gift.service;

import gift.dto.MemberDTO;
import gift.exception.InvalidLoginException;
import gift.exception.EmailAlreadyExistsException;
import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public MemberDTO register(MemberDTO memberDTO) {
        Optional<Member> existingMember = memberRepository.findByEmail(memberDTO.getEmail());
        if (existingMember.isPresent()) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }

        Member newMember = Member.createWithEncodedPassword(null, memberDTO.getEmail(), memberDTO.getPassword());
        Member savedMember = memberRepository.save(newMember);
        return convertToDTO(savedMember);
    }

    public String login(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
            if (encodedPassword.equals(member.get().getPassword())) {
                return jwtUtil.generateToken(member.get().getEmail());
            }
        }
        throw new InvalidLoginException("Invalid email or password");
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    private MemberDTO convertToDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        return memberDTO;
    }
}
