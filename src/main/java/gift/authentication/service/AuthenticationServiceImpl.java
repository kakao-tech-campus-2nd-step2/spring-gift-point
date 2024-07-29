package gift.authentication.service;

import gift.core.domain.authentication.AuthenticationService;
import gift.core.domain.authentication.Token;
import gift.core.domain.authentication.exception.AuthenticationFailedException;
import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserAccountRepository;
import gift.core.domain.user.exception.UserAccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokenProvider tokenProvider;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public AuthenticationServiceImpl(
            TokenProvider tokenProvider,
            UserAccountRepository userAccountRepository
    ) {
        this.tokenProvider = tokenProvider;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Token authenticate(String principal, String credentials) {
        if (!userAccountRepository.existsByPrincipal(principal)) {
            throw new UserAccountNotFoundException();
        }
        UserAccount userAccount = userAccountRepository
                .findByPrincipal(principal)
                .orElseThrow(UserAccountNotFoundException::new);
        if (!userAccount.credentials().equals(credentials)) {
            throw new AuthenticationFailedException();
        }
        Long userId = userAccountRepository.findUserIdByPrincipal(principal);
        return tokenProvider.generateAccessToken(userId);
    }
}
