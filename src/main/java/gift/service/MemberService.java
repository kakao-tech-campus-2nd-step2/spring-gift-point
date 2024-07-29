package gift.service;

import gift.model.Member;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member register(String email, String password) {
        if (memberRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        String encodedPassword = password.isEmpty() ? "" : passwordEncoder.encode(password);
        Member member = new Member(email, encodedPassword);
        return memberRepository.save(member);
    }

    @Transactional
    public Member register(Member member) {
        if (memberRepository.findByEmail(member.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        member.encodePassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        if (member != null && passwordEncoder.matches(password, member.getPassword())) {
            return member;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }
}