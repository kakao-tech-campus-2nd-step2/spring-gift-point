package gift.domain.service.member;

import gift.domain.dto.request.member.LocalMemberRequest;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import gift.domain.repository.LocalMemberRepository;
import gift.global.util.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalMemberService implements DerivedMemberService<LocalMember, LocalMemberRequest> {

    private static final Logger log = LoggerFactory.getLogger(LocalMemberService.class);
    private final LocalMemberRepository localMemberRepository;

    public LocalMemberService(LocalMemberRepository localMemberRepository) {
        this.localMemberRepository = localMemberRepository;
    }

    @Override
    @Transactional
    public Member registerMember(MemberRequest requestDto, Member member) {
        LocalMember localMember = localMemberRepository.save(convert(requestDto).toEntity(member));
        member.setLocalMember(localMember);
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public Member loginMember(MemberRequest requestDto, Member member) {
        LocalMember localMember = findDerivedMemberBy(member);

        // 유저는 존재하나 비밀번호가 맞지 않은 채 로그인 시도
        if (!HashUtil.hashCode(convert(requestDto).getPassword()).equals(localMember.getPassword())) {
            throw new ServerException(ErrorCode.MEMBER_INCORRECT_LOGIN_INFO);
        }

        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public LocalMember findDerivedMemberBy(Member member) {
        return localMemberRepository.findByMember(member).orElseThrow(() -> new ServerException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public LocalMemberRequest convert(MemberRequest requestDto) {
        if (requestDto instanceof LocalMemberRequest) {
            return (LocalMemberRequest) requestDto;
        }
        log.error("Type conversion was invalid! requestDto type was {}.", requestDto.getClass().getTypeName());
        throw new IllegalStateException("Type Conversion invalid! You need debugging!");
    }
}
