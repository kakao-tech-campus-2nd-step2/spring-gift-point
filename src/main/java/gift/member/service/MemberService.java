package gift.member.service;

import gift.member.domain.Member;
import gift.member.dto.MemberServiceDto;
import gift.member.exception.DuplicateEmailException;
import gift.member.exception.DuplicateNicknameException;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public Member createMember(MemberServiceDto memberServiceDto) {
        validateEmailAndNicknameUnique(memberServiceDto);
        return memberRepository.save(memberServiceDto.toMember());
    }

    public Member updateMember(MemberServiceDto memberServiceDto) {
        Member existingMember = validateMemberExists(memberServiceDto.id());
        validateEmailAndNicknameUnique(memberServiceDto, existingMember);
        return memberRepository.save(memberServiceDto.toMember());
    }

    public void deleteMember(Long id) {
        validateMemberExists(id);
        memberRepository.deleteById(id);
    }

    public Long getPoint(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new).getPoint();
    }

    private Member validateMemberExists(Long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    private void validateEmailAndNicknameUnique(MemberServiceDto memberServiceDto) {
        if (memberRepository.existsByEmail(memberServiceDto.email())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByNickname(memberServiceDto.nickName())) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateEmailAndNicknameUnique(MemberServiceDto memberServiceDto, Member existingMember) {
        boolean isEmailChanged = !existingMember.getEmail().equals(memberServiceDto.email());
        boolean isNicknameChanged = !existingMember.getNickname().equals(memberServiceDto.nickName());

        if (isEmailChanged && memberRepository.existsByEmail(memberServiceDto.email())) {
            throw new DuplicateEmailException();
        }

        if (isNicknameChanged && memberRepository.existsByNickname(memberServiceDto.nickName())) {
            throw new DuplicateNicknameException();
        }
    }

}
