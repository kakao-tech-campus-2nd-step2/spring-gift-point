package gift.member.service;

import gift.member.domain.Member;
import gift.member.exception.MemberAlreadyExistsException;
import gift.member.exception.MemberNotFoundException;
import gift.member.persistence.MemberRepository;
import gift.member.service.command.MemberInfoCommand;
import gift.member.service.dto.MemberSignInInfo;
import gift.member.service.dto.MemberSignupInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public MemberSignupInfo signUp(MemberInfoCommand memberInfoCommand) {
        memberRepository.findByUsername(memberInfoCommand.username())
                .ifPresent(u -> {
                    throw new MemberAlreadyExistsException();
                });

        Member newMember = memberInfoCommand.toEntity();

        memberRepository.save(newMember);

        String token = jwtProvider.generateToken(newMember.getId(), newMember.getUsername(), newMember.getRole());

        return MemberSignupInfo.of(newMember.getId(), token);
    }

    @Transactional(readOnly = true)
    public MemberSignInInfo signIn(MemberInfoCommand memberInfoCommand) {
        Member savedMember = memberRepository.findByUsername(memberInfoCommand.username())
                .orElseThrow(MemberNotFoundException::new);
        if (savedMember.isNotOAuthAccount()) {
            checkPassword(memberInfoCommand, savedMember);
        }

        String token = jwtProvider.generateToken(savedMember.getId(), savedMember.getUsername(), savedMember.getRole());

        return MemberSignInInfo.of(token);
    }

    @Transactional(readOnly = true)
    public boolean checkUser(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional(readOnly = true)
    public String getUsername(final Long userId) {
        var savedMember = memberRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        return savedMember.getUsername();
    }

    private void checkPassword(MemberInfoCommand memberInfoCommand, Member savedMember) {
        if (PasswordProvider.match(memberInfoCommand.username(), memberInfoCommand.password(),
                savedMember.getPassword())) {
            throw new MemberNotFoundException();
        }
    }

    public Integer getPoint(Long memberId) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return member.getPoint();
    }
}
