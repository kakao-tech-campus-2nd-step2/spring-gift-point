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
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member createMember(MemberServiceDto memberServiceDto) {
        validateEmailAndNicknameUnique(memberServiceDto);
        return memberRepository.save(memberServiceDto.toMember());
    }

    public Member updateMember(MemberServiceDto memberServiceDto) {
        validateMemberExists(memberServiceDto.id());
        validateEmailAndNicknameUnique(memberServiceDto);
        return memberRepository.save(memberServiceDto.toMember());
    }

    public void deleteMember(Long id) {
        validateMemberExists(id);
        memberRepository.deleteById(id);
    }

    private void validateMemberExists(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }
    }

    private void validateEmailAndNicknameUnique(MemberServiceDto memberServiceDto) {
        if (memberRepository.existsByEmail(memberServiceDto.email())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByNickname(memberServiceDto.nickName())) {
            throw new DuplicateNicknameException();
        }
    }
}
