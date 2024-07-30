package gift.service;

import gift.domain.LoginType;
import gift.domain.Member;
import gift.dto.request.MemberRequest;
import gift.dto.response.KakaoProfileResponse;
import gift.dto.response.KakaoTokenResponse;
import gift.exception.DuplicateMemberEmailException;
import gift.exception.InvalidCredentialsException;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public Map<String, String> handleKakaoLogin(String code) {
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

        Map<String, String> response = new HashMap<>();
        response.put("authorizationCode", code);
        response.put("accessToken", accessToken);
        response.put("email", email);

        return response;
    }

}
