package gift.member.service;

import gift.global.util.JwtProvider;
import gift.member.dto.MemberDto;
import gift.member.dto.MemberPasswordRequest;
import gift.member.dto.MemberResponse;
import gift.member.entity.Member;
import gift.member.exception.AlreadyExistMemberException;
import gift.member.exception.InvalidPasswordException;
import gift.member.exception.NoSuchMemberException;
import gift.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public Page<MemberDto> findMembers(Pageable pageable) {
        return memberRepository.findAll(pageable)
            .map(member -> member.toDto());
    }

    public MemberDto findMember(String email) {
        return memberRepository.findById(email)
            .orElseThrow(NoSuchMemberException::new)
            .toDto();
    }

    public MemberResponse register(MemberDto memberDto) {
        try {
            findMember(memberDto.email());
            throw new AlreadyExistMemberException();
        } catch (NoSuchMemberException e) {
            MemberDto savedMemberDto = memberRepository.save(memberDto.toEntity()).toDto();
            return new MemberResponse(jwtProvider.createAccessToken(savedMemberDto));
        }
    }

    public MemberResponse login(MemberDto memberDto) {
        MemberDto foundMemberDto = findMember(memberDto.email());
        checkPassword(memberDto.password(), foundMemberDto.password());
        return new MemberResponse(jwtProvider.createAccessToken(memberDto));
    }

    public MemberResponse changePassword(Member member, MemberPasswordRequest memberPasswordRequest) {
        checkPassword(memberPasswordRequest.password(), member.getPassword());
        member.changePassword(memberPasswordRequest.newPassword1());
        MemberDto updatedMemberDto = memberRepository.save(member).toDto();
        return new MemberResponse(jwtProvider.createAccessToken(updatedMemberDto));
    }

    private void checkPassword(String password, String expectedPassword) {
        if (!password.equals(expectedPassword)){
            throw new InvalidPasswordException();
        }
    }
}
