package gift.service;

import gift.domain.Member;
import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;

import static gift.LoginType.EMAIL;
import static gift.LoginType.KAKAO;
import static gift.exception.ErrorCode.KAKAO_LOGIN_USER;


@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public AuthService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
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

}
