package gift.auth.service;

import gift.auth.client.KakaoApiClient;
import gift.auth.dto.KakaoUserInfo;
import gift.auth.dto.LoginReqDto;
import gift.auth.exception.LoginFailedException;
import gift.auth.token.AuthToken;
import gift.auth.token.AuthTokenGenerator;
import gift.member.dto.MemberResDto;
import gift.member.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberService memberService;
    private final AuthTokenGenerator authTokenGenerator;
    private final KakaoApiClient kakaoApiClient;

    public AuthService(MemberService memberService, AuthTokenGenerator authTokenGenerator, KakaoApiClient kakaoApiClient) {
        this.memberService = memberService;
        this.authTokenGenerator = authTokenGenerator;
        this.kakaoApiClient = kakaoApiClient;
    }

    public AuthToken login(String header, LoginReqDto loginReqDto) {

        Long memberId = authTokenGenerator.extractMemberId(header);

        // 회원 정보 조회
        MemberResDto member = memberService.getMember(memberId);
        String password = memberService.getMemberPassword(memberId);

        // 이메일과 비밀번호가 일치하지 않으면 예외 발생
        if (!member.email().equals(loginReqDto.email()) || !password.equals(loginReqDto.password())) {
            throw LoginFailedException.EXCEPTION;
        }

        return authTokenGenerator.generateToken(member);
    }

    public AuthToken login(String code) {
        String accessToken = kakaoApiClient.getAccessToken(code);
        KakaoUserInfo userInfo = kakaoApiClient.getUserInfo(accessToken);

        // 카카오로 로그인한 회원 정보 조회 및 카카오 토큰 저장
        // 카카오로 로그인한 회원이 없으면 회원 가입
        MemberResDto member = memberService.loginOrRegisterByEmail(userInfo.kakaoAccount().email(), accessToken);

        // 토큰 생성
        return authTokenGenerator.generateToken(member);
    }
}
