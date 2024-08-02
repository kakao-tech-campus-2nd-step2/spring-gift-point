package gift.service;

import gift.LoginType;
import gift.domain.Member;
import gift.dto.KakaoTokenInfo;
import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

import static gift.LoginType.EMAIL;
import static gift.LoginType.KAKAO;
import static gift.exception.ErrorCode.ALREADY_REGISTERED_ERROR;
import static gift.exception.ErrorCode.KAKAO_LOGIN_USER;


@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final KaKaoService kaKaoService;

    public AuthService(MemberRepository memberRepository, JwtUtil jwtUtil, KaKaoService kaKaoService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.kaKaoService = kaKaoService;
    }

    public AuthResponse addMember(AuthRequest authRequest) {
        Member requestMember = new Member(authRequest.email(), authRequest.password(), EMAIL);
        Member savedMember = memberRepository.save(requestMember);
        return new AuthResponse(jwtUtil.createJWT(savedMember.getId(), savedMember.getLoginType()));
    }

    public AuthResponse login(AuthRequest authRequest) {
        Member storedMember = memberRepository.findMemberByEmail(authRequest.email()).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        if (storedMember.getLoginType().equals(KAKAO)) {
            throw new CustomException(KAKAO_LOGIN_USER);
        }
        return new AuthResponse(jwtUtil.createJWT(storedMember.getId(), storedMember.getLoginType()));
    }

    public AuthResponse kakaoLogin(String code) {
        KakaoTokenInfo kakaoTokenInfo = kaKaoService.getKakaoTokenInfo(code);
        String email = kaKaoService.getKakaoAccountEmail(kakaoTokenInfo.access_token());

        Member member = findOrSaveMember(email, kakaoTokenInfo.access_token(), kakaoTokenInfo.refresh_token());
        if (member.getLoginType().equals(EMAIL)) {
            throw new CustomException(ALREADY_REGISTERED_ERROR);
        }

        return new AuthResponse(jwtUtil.createJWT(member.getId(), member.getLoginType()));
    }

    private Member findOrSaveMember(String email, String accessToken, String refreshToken) {
        Optional<Member> existMember = memberRepository.findMemberByEmail(email);
        return existMember.orElseGet(() -> memberRepository.save(new Member(email, generateRandomPassword(), LoginType.KAKAO, accessToken, refreshToken)));
    }

    private String generateRandomPassword() {
        Random random = new Random();
        return String.valueOf(1000 + random.nextInt(9000));
    }

}
