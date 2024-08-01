package gift.member.service;

import gift.auth.service.AuthenticationTool;
import gift.member.dto.MemberRequest;
import gift.member.entity.Member;
import gift.repository.JpaMemberRepository;
import gift.auth.dto.LoginMemberToken;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import gift.exceptionAdvisor.exceptions.GiftUnauthorizedException;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MemberService{

    private final JpaMemberRepository jpaMemberRepository;

    private final AuthenticationTool authenticationTool;

    public MemberService(JpaMemberRepository jpaMemberRepository,
        AuthenticationTool authenticationTool) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.authenticationTool = authenticationTool;
    }

    public void register(MemberRequest memberRequest) {
        if (jpaMemberRepository.existsByEmail(memberRequest.getEmail())) {
            throw new GiftUnauthorizedException("사용할 수 없는 이메일입니다.");
        }
        Member member = new Member(null, memberRequest.getEmail(), memberRequest.getPassword(),
            memberRequest.getRole());
        jpaMemberRepository.save(member);
    }

    public LoginMemberToken login(MemberRequest memberRequest) {
        Member member = findByEmail(memberRequest.getEmail());

        if (memberRequest.getPassword().equals(member.getPassword())) {
            String token = authenticationTool.makeToken(member);
            return new LoginMemberToken(token);
        }

        throw new GiftUnauthorizedException("로그인에 실패하였습니다.");
    }

    public boolean checkRole(MemberRequest memberRequest) {
        return false;
    }

    public MemberRequest getLoginUser(String token) {
        long id = authenticationTool.parseToken(token);
        Member member = jpaMemberRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("회원이 존재하지 않습니다."));

        return new MemberRequest(id, member.getEmail(), member.getPassword(), member.getRole());
    }

    private Member findByEmail(String email) {
        try {
            return jpaMemberRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            throw new GiftNotFoundException("회원이 존재하지 않습니다.");
        }
    }
}
