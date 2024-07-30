package gift.service;

import gift.domain.LoginType;
import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoLoginResponse;
import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.TokenResponse;
import gift.exception.DuplicateMemberEmailException;
import gift.exception.InvalidCredentialsException;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static gift.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private static final String KAKAO_EMAIL_SUFFIX = "@kakao.com";

    private final MemberSpringDataJpaRepository memberRepository;
    private final KakaoAuthService kakaoAuthService;
    private final TokenService tokenService;


    @Autowired
    public MemberService(MemberSpringDataJpaRepository memberRepository, KakaoAuthService kakaoAuthService, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.kakaoAuthService = kakaoAuthService;
        this.tokenService = tokenService;
    }

    @Transactional
    public Member register(MemberRequest memberRequest, LoginType loginType) {
        Optional<Member> oldMember = memberRepository.findByEmailAndLoginType(memberRequest.getEmail(), loginType);

        if (oldMember.isPresent()) {
            throw new DuplicateMemberEmailException(DUPLICATE_MEMBER_EMAIL);
        }
        Member member = new Member(memberRequest.getEmail(), memberRequest.getPassword(), loginType);
        memberRepository.save(member);
        return member;
    }

    public Member authenticate(MemberRequest memberRequest, LoginType loginType) {
        Member member = memberRepository.findByEmailAndLoginType(memberRequest.getEmail(), loginType)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        if (!memberRequest.getPassword().equals(member.getPassword())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIAL);
        }
        return member;
    }

    public Member findByEmailAndLoginType(String email, LoginType loginType) {
        return memberRepository.findByEmailAndLoginType(email, loginType)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
    }

    @Transactional
    public TokenResponse handleNormalRegister(MemberRequest memberRequest) {
        Member member = register(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse handleNormalAuthenticate(MemberRequest memberRequest) {
        Member member = authenticate(memberRequest, NORMAL);
        String token = tokenService.saveToken(member);
        return new TokenResponse(token);
    }

    @Transactional
    public KakaoLoginResponse handleKakaoLogin(String code) {
        KakaoTokenResponse tokenResponse = kakaoAuthService.getKakaoToken(code);
        KakaoProfileResponse profileResponse = kakaoAuthService.getUserProfile(tokenResponse.accessToken());

        String email = profileResponse.kakaoAccount().profile().nickname() +  KAKAO_EMAIL_SUFFIX;

        if (KAKAO_EMAIL_SUFFIX.equals(email)) {
            throw new RuntimeException("이메일을 가져올 수 없습니다.");
        }

        LoginType loginType = LoginType.KAKAO;
        Member member;
        try {
            member = register(new MemberRequest(email, "kakao"), loginType);
        } catch (DuplicateMemberEmailException e) {
            member = findByEmailAndLoginType(email, loginType);
        }

        String accessToken = tokenService.saveToken(member, tokenResponse.accessToken());

        return new KakaoLoginResponse(code, accessToken, email);
    }

}
