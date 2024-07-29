package gift.service;

import gift.constants.Messages;
import gift.domain.Member;
import gift.dto.MemberRequestDto;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;
import org.aspectj.bridge.Message;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(MemberRequestDto memberRequestDto){
        memberRepository.save(memberRequestDto.toEntity());
    }

    public void authenticate(String email,String password){
        memberRepository.findByEmailAndPassword(email,password)
            .orElseThrow(() -> new MemberNotFoundException("등록된 유저가 존재하지 않습니다"));
    }

    public void oauthSave(String email, String password, String accessToken) {
        memberRepository.save(new Member(email,password,accessToken));
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email).orElseThrow(()->new MemberNotFoundException(
            Messages.NOT_FOUND_MEMBER_BY_EMAIL_MESSAGE));
    }
}
