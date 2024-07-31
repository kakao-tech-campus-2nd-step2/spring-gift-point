package gift.common.auth;

import gift.common.exception.MemberException;
import gift.common.utils.TokenProvider;
import gift.member.MemberErrorCode;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.member.model.MemberRequest;
import gift.member.model.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberResponse getToken(MemberRequest memberRequest) throws MemberException {
        Member member = memberRepository.findByEmail(memberRequest.email());
        if (!member.matchPassword(memberRequest.password())) {
            throw new MemberException(MemberErrorCode.FAILURE_LOGIN);
        }
        return new MemberResponse("Bearer", tokenProvider.generateToken(member));
    }

    public boolean validateAuthorization(String authorizationHeader) {
        if (authorizationHeader == null) {
            return false;
        }
        String type = extractType(authorizationHeader);
        String token = extractToken(authorizationHeader);
        return isBearer(type) && tokenProvider.validateToken(token);
    }

    public Long getMemberId(String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        return Long.parseLong(tokenProvider.getClaims(token).getSubject());
    }

    private boolean isBearer(String type) {
        return type.equals("Bearer");
    }

    private String extractType(String authorizationHeader) {
        return authorizationHeader.split(" ")[0];
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.split(" ")[1];
    }
}
