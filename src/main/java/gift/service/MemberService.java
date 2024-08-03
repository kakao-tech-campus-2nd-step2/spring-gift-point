package gift.service;

import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.dto.response.MemberResponse;
import gift.exception.customException.DuplicateMemberEmailException;
import gift.exception.customException.MemberNotFoundException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gift.exception.errorMessage.Messages.MEMBER_EMAIL_ALREADY_EXISTS;
import static gift.exception.errorMessage.Messages.NOT_FOUND_MEMBER;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void save(MemberRequest memberRequest){
        if(memberRepository.existsByEmail(memberRequest.email())){
            throw new DuplicateMemberEmailException(MEMBER_EMAIL_ALREADY_EXISTS);
        }
        memberRepository.save(new Member(memberRequest.email(), memberRequest.password(), 3000));
    }

    @Transactional(readOnly = true)
    public boolean checkMemberExistsByEmailAndPassword(String email, String password) {
        memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
        return true;
    }

    @Transactional(readOnly = true)
    public MemberResponse findByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public int getMemberPoints(MemberRequest memberRequest){
        Member member = memberRepository.findById(memberRequest.id())
                .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
        return member.getPoints();
    }

    @Transactional
    public void addMemberPoints(MemberRequest memberRequest, int addPoints){
        Member member = memberRepository.findById(memberRequest.id())
                .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
        member.addPoints(addPoints);
    }

    @Transactional
    public void useMemberPoints(MemberRequest memberRequest, int usePoints){
        Member member = memberRepository.findById(memberRequest.id())
                .orElseThrow(() -> new MemberNotFoundException(NOT_FOUND_MEMBER));
        member.usePoints(usePoints);
    }
}
