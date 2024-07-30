package gift.service;

import gift.auth.JwtHelper;
import gift.auth.Token;
import gift.repository.MemberRepository;
import gift.vo.Member;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final JwtHelper jwtHelper;
    private final MemberRepository repository;

    public MemberService(MemberRepository repository, JwtHelper jwtHelper) {
        this.repository = repository;
        this.jwtHelper = jwtHelper;
    }

    public Token login(Member member) {
        Member foundMember = repository.findByEmail(member.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        foundMember.validateEmail(foundMember.getEmail());
        String jwtToken = createJwtToken(foundMember.getId(), foundMember.getEmail());
        return new Token(jwtToken);
    }

    public Token join(Member member) {
        Member joinedMember = repository.save(member);
        return login(joinedMember);
    }

    public String createJwtToken(Long memberId, String email) {
        return jwtHelper.generateToken(memberId, email);
    }

    public Member getMemberById(Long id) {
        return repository.findById(id).orElseThrow( () -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. "));
    }

    public Member getMemberByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다. "));
    }

    public boolean hasMemberByEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }
}
