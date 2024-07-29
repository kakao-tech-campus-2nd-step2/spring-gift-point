package gift.service;

import gift.domain.AuthToken;
import gift.domain.TokenInformation;
import gift.exception.customException.EmailDuplicationException;
import gift.exception.customException.UnAuthorizationException;
import gift.repository.token.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static gift.exception.exceptionMessage.ExceptionMessage.ALREADY_TOKEN_GET_EMAIL;
import static gift.utils.TokenConstant.EXPIRATION_OFFSET;

@Service
@Transactional(readOnly = true)
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public String tokenSave(String token, String email) {
        Optional<AuthToken> tokenByEmail = tokenRepository.findTokenByEmail(email);

        if (tokenByEmail.isPresent()) {
            throw new EmailDuplicationException(ALREADY_TOKEN_GET_EMAIL);
        }

        AuthToken authToken = new AuthToken(token, email);
        tokenRepository.save(authToken);
        return token;
    }

    public AuthToken findToken(String token) {
        return tokenRepository.findAuthTokenByToken(token)
                .orElseThrow(UnAuthorizationException::new);
    }

    public AuthToken findTokenById(Long tokenId) {
        return tokenRepository.findById(tokenId)
                .orElseThrow(UnAuthorizationException::new);
    }

    @Transactional
    public AuthToken oauthTokenSave(TokenInformation tokenInfo, String email) {
        UUID uuid = UUID.randomUUID();

        AuthToken authToken = new AuthToken.Builder()
                .token(uuid.toString())
                .tokenTime(tokenInfo.getAccessTokenTime() - EXPIRATION_OFFSET)
                .email(email)
                .accessToken(tokenInfo.getAccessToken())
                .accessTokenTime(tokenInfo.getAccessTokenTime())
                .refreshToken(tokenInfo.getRefreshToken())
                .refreshTokenTime(tokenInfo.getRefreshTokenTime())
                .build();

        return tokenRepository.save(authToken);
    }

    @Transactional
    public AuthToken updateToken(Long tokenId, TokenInformation tokenInfo) {
        AuthToken findToken = findTokenById(tokenId);

        findToken.update(tokenInfo);

        return findToken;
    }
}
