package gift.service;

import gift.dto.request.MemberRequest;
import gift.dto.response.MemberInfoResponse;
import gift.entity.Member;
import gift.exception.EmailDuplicateException;
import gift.exception.MemberNotFoundException;
import gift.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long register(MemberRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new EmailDuplicateException(request.email());
        }
        Member member = new Member(request.email(), request.password());
        return memberRepository.save(member).getId();
    }


    public Long login(MemberRequest request) {
        Member registeredMember = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(MemberNotFoundException::new);
        return registeredMember.getId();
    }

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<MemberInfoResponse> getAllMember() {
        return memberRepository.findAll()
                .stream()
                .map(MemberInfoResponse::fromMember)
                .toList();
    }

    @Transactional
    public void updateMemberPoint(Long memberId, int newPoint) {
        memberRepository.getReferenceById(memberId)
                .updatePoint(newPoint);
    }

    public Long findOrSaveMemberAndGetMemberIdByEmail(String email) {
        return memberRepository.findByEmail(email)
                .map(Member::getId)
                .orElseGet(() ->
                        memberRepository.save(new Member(email, "kakaoMember")).getId()
                );
    }
}
