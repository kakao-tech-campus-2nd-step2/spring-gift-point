package gift.service;

import gift.domain.Member;
import gift.dto.request.AuthRequest;
import gift.dto.response.AuthResponse;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    public AuthService(MemberRepository memberRepository, JwtUtil jwtUtil) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse addMember(AuthRequest authRequest) {
        Member requestMember = new Member(authRequest.email(), authRequest.password());
        Member savedMember = memberRepository.save(requestMember);
        return new AuthResponse(jwtUtil.createJWT(savedMember.getId()));
    }

    public AuthResponse login(AuthRequest authRequest) {
        Member storedMember = memberRepository.findMemberByEmail(authRequest.email()).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        return new AuthResponse(jwtUtil.createJWT(storedMember.getId()));
    }

}
