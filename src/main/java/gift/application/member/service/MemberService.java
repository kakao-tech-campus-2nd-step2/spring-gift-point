package gift.application.member.service;

import gift.global.auth.jwt.JwtProvider;
import gift.model.member.Member;
import gift.global.validate.InvalidAuthRequestException;
import gift.global.validate.NotFoundException;
import gift.model.member.Provider;
import gift.repository.member.MemberRepository;
import gift.application.member.dto.MemberCommand;
import gift.application.member.dto.MemberModel;
import org.springframework.data.util.Pair;
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
    public MemberModel.Info register(MemberCommand.Create command) {
        if (memberRepository.existsByEmail(command.email())) {
            throw new InvalidAuthRequestException("User already exists.");
        }
        var member = memberRepository.save(command.toEntity());
        return MemberModel.Info.from(member);
    }

    @Transactional(readOnly = true)
    public MemberModel.InfoAndJwt login(MemberCommand.Login command) {
        Member member = memberRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidAuthRequestException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!member.verifyPassword(command.password())) {
            throw new InvalidAuthRequestException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        String jwt = jwtProvider.createToken(member.getId(), member.getRole());
        return new MemberModel.InfoAndJwt(MemberModel.Info.from(member), jwt);
    }

    @Transactional(readOnly = true)
    public MemberModel.Info getUser(Long memberId) {
        var member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("User not found."));
        return MemberModel.Info.from(member);
    }

    @Transactional
    public MemberModel.IdAndJwt socialLogin(MemberCommand.Create create) {
        var member = memberRepository.findByEmail(create.email());
        if (member.isPresent()) {
            var originMember = member.get();
            originMember.changeProvider(Provider.KAKAO);
            String jwt = jwtProvider.createToken(originMember.getId(), originMember.getRole());
            return new MemberModel.IdAndJwt(originMember.getId(), jwt);
        }

        var savedMember = memberRepository.save(create.toEntity());
        String jwt = jwtProvider.createToken(savedMember.getId(), savedMember.getRole());
        return new MemberModel.IdAndJwt(savedMember.getId(), jwt);
    }
}
