package gift.auth.application;

import gift.auth.dto.AuthResponse;
import gift.auth.util.CustomPasswordGenerator;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.global.security.JwtUtil;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.client.KakaoClient;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberRequest;
import gift.member.entity.Member;
import gift.member.util.KakaoTokenMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KakaoClient kakaoClient;

    private static final String KAKAO_EMAIL_PREFIX = "kakao_user";
    private static final String KAKAO_EMAIL_SUFFIX = "@kakao.com";

    public AuthService(MemberRepository memberRepository,
                       JwtUtil jwtUtil,
                       KakaoClient kakaoClient) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kakaoClient = kakaoClient;
    }

    public AuthResponse authenticate(MemberRequest memberRequest) {
        Member member = memberRepository.findByEmail(memberRequest.email())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getPassword()
                   .equals(memberRequest.password())) {
            throw new CustomException(ErrorCode.AUTHENTICATION_FAILED);
        }

        return generateAuthResponse(member);
    }

    public AuthResponse generateAuthResponse(Member member) {
        return AuthResponse.of(
                jwtUtil.generateToken(member.getId())
        );
    }

    public AuthResponse authenticate(String code) {
        KakaoTokenResponse tokenResponse = kakaoClient.getTokenResponse(code);
        Long kakaoUserId = kakaoClient.getUserId(tokenResponse.accessToken());
        String email = KAKAO_EMAIL_PREFIX + kakaoUserId + KAKAO_EMAIL_SUFFIX;

        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    String password = CustomPasswordGenerator.generateTemporaryPassword();
                    Member newMember = new Member(
                            email,
                            password,
                            KakaoTokenMapper.toTokenInfo(tokenResponse)
                    );
                    return memberRepository.save(newMember);
                });

        return generateAuthResponse(member);
    }

}
