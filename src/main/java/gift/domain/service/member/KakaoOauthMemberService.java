package gift.domain.service.member;

import gift.domain.dto.request.member.KakaoOauthMemberRequest;
import gift.domain.dto.request.member.MemberRequest;
import gift.domain.entity.KakaoOauthMember;
import gift.domain.entity.Member;
import gift.domain.exception.conflict.MemberAlreadyExistsException;
import gift.domain.exception.forbidden.MemberIncorrectLoginInfoException;
import gift.domain.exception.notFound.MemberNotFoundException;
import gift.domain.repository.KakaoOauthMemberRepository;
import gift.domain.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KakaoOauthMemberService implements DerivedMemberService<KakaoOauthMember, KakaoOauthMemberRequest> {

    private static final Logger log = LoggerFactory.getLogger(KakaoOauthMemberService.class);
    private final KakaoOauthMemberRepository kakaoOauthMemberRepository;
    private final OauthService oauthService;

    public KakaoOauthMemberService(KakaoOauthMemberRepository kakaoOauthMemberRepository, OauthService oauthService) {
        this.kakaoOauthMemberRepository = kakaoOauthMemberRepository;
        this.oauthService = oauthService;
    }

    @Override
    @Transactional
    public Member registerMember(MemberRequest requestDto, Member member) {
        Long kakaoIdentifier = getKakaoIdentifier(requestDto);
        kakaoOauthMemberRepository.findByKakaoIdentifier(kakaoIdentifier).ifPresent(o -> {
                throw new MemberAlreadyExistsException();
        });
        KakaoOauthMember kakaoOauthMember = kakaoOauthMemberRepository.save(convert(requestDto).toEntity(member, kakaoIdentifier));
        member.setKakaoOauthMember(kakaoOauthMember);
        return member;
    }

    @Override
    @Transactional
    public Member loginMember(MemberRequest requestDto, Member member) {
        KakaoOauthMember kakaoOauthMember = findDerivedMemberBy(member);
        Long kakaoIdentifier = getKakaoIdentifier(requestDto);

        // 카카오 식별자가 다른 경우 에러
        if (!kakaoOauthMember.getKakaoIdentifier().equals(kakaoIdentifier)) {
            throw new MemberIncorrectLoginInfoException();
        }

        kakaoOauthMember.setAccessToken(convert(requestDto).getAccessToken());
        return member;
    }

    @Override
    @Transactional(readOnly = true)
    public KakaoOauthMember findDerivedMemberBy(Member member) {
        return kakaoOauthMemberRepository.findByMember(member).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public KakaoOauthMemberRequest convert(MemberRequest requestDto) {
        if (requestDto instanceof KakaoOauthMemberRequest) {
            return (KakaoOauthMemberRequest) requestDto;
        }

        log.error("Type conversion was invalid! requestDto type was {}.", requestDto.getClass().getTypeName());
        throw new IllegalStateException("Type Conversion invalid! You need debugging!");
    }

    private Long getKakaoIdentifier(MemberRequest requestDto) {
        return oauthService.getKakaoUserInfo(convert(requestDto).getAccessToken()).id();
    }
}
