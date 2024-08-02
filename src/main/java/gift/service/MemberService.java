package gift.service;


import gift.dto.KakaoMember;
import gift.model.Member;
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

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.jwtUtil = new JwtUtil();
    }
    public Member registerMember(Member member) {
        member.setPassword(member.getPassword());
        return memberRepository.save(member);
    }

    public Optional<Member> authenticate(String email, String password) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member. isPresent() && member.get().getPassword().equals(password)) {
            return member;
        }
        return Optional.empty();
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElse(null); //카카오 컨트롤러에서 토큰을 통해 멤버 불러오기 위해 생성
    }

    public void updateMember(Member member) {
        memberRepository.save(member); //멤버에 토큰을 넣어서 다시 저장하기 위함
    }
    @Transactional
    public String loginKakaoMember(KakaoMember kakaoMember) {
        Member member = memberRepository.findByEmail(kakaoMember.email())
            .orElseGet(() -> memberRepository.save(new Member(kakaoMember.email(), kakaoMember.password())));
        return jwtUtil.generateToken(member.getId(),member.getPassword());
    }
}