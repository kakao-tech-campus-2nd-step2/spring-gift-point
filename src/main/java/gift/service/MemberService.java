package gift.service;

import gift.domain.Auth.LoginRequest;
import gift.domain.Member.Member;
import gift.domain.Member.MemberRequest;
import gift.domain.WishList.WishList;
import gift.repository.MemberRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.NoSuchElementException;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private JwtService jwtService;

    public MemberService(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    public void join(MemberRequest memberRequest) throws BadRequestException {
        if (!memberRepository.existsById(memberRequest.id())) {
            memberRepository.save(new Member(memberRequest.id(), memberRequest.password(), memberRequest.name(),new LinkedList<WishList>()));
            return;
        }
        throw new BadRequestException("이미 존재하는 회원입니다.");
    }

    public String login(LoginRequest loginRequest) {
        Member dbMember = memberRepository.findById(loginRequest.email())
                .orElseThrow(() -> new NoSuchElementException("로그인에 실패했습니다 다시 시도해주세요"));
        if (!dbMember.checkPassword(loginRequest.password())){
            throw new NoSuchElementException("로그인에 실패하였습니다. 다시 시도해주세요");
        } else {
            String jwt = jwtService.createJWT(loginRequest.email());
            return jwt;
        }
    }

    public Member findById(String Id) {
        return memberRepository.findById(Id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원 정보가 없습니다."));
    }
}
