package gift.domain.service.member;

import gift.domain.dto.request.member.MemberRequest;
import gift.domain.dto.response.MemberResponse;
import gift.domain.entity.Member;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import gift.domain.repository.MemberRepository;
import gift.global.WebConfig.Constants.Domain.Member.Permission;
import gift.global.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final DerivedMemberService.Factory memberServiceFactory;
    private final JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, DerivedMemberService.Factory memberServiceFactory, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.memberServiceFactory = memberServiceFactory;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public MemberResponse registerMember(MemberRequest requestDto) {
        // 기존 회원인 경우 예외
        memberRepository.findByEmail(requestDto.getEmail()).ifPresent(p -> {
            throw new ServerException(ErrorCode.MEMBER_ALREADY_EXISTS);
        });
        Member member = memberRepository.save(requestDto.toEntity(Permission.MEMBER));
        return new MemberResponse(jwtUtil.generateToken(memberServiceFactory.getInstance(requestDto).registerMember(requestDto, member)));
    }

    @Transactional(readOnly = true)
    public MemberResponse loginMember(MemberRequest requestDto) {
        // 존재하지 않은 이메일을 가진 유저로 로그인 시도한 경우 예외
        Member member = memberRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(() -> new ServerException(ErrorCode.MEMBER_INCORRECT_LOGIN_INFO));
        return new MemberResponse(jwtUtil.generateToken(memberServiceFactory.getInstance(requestDto).loginMember(requestDto, member)));
    }

    @Transactional
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new ServerException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
