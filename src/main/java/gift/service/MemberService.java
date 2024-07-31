package gift.service;

import gift.model.Member;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  public String register(Member member) {
    Member savedMember = memberRepository.save(member);
    return jwtUtil.generateToken(savedMember.getId(), savedMember.getEmail());
  }

  public String login(String email, String password) {
    return memberRepository.findByEmail(email)
            .filter(member -> member.getPassword().equals(password))
            .map(member -> jwtUtil.generateToken(member.getId(), member.getEmail()))
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
  }

  public Optional<Member> findById(Long memberId) {
    return memberRepository.findById(memberId);
  }
}
