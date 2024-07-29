package gift.authentication;

import gift.authentication.service.AuthenticationServiceImpl;
import gift.authentication.service.TokenProvider;
import gift.core.domain.authentication.AuthenticationService;
import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private TokenProvider tokenProvider;

    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        authenticationService = new AuthenticationServiceImpl(tokenProvider, userAccountRepository);
    }

    @Test
    public void testAuthenticate() {
        String principal = "test";
        String credentials = "test";
        UserAccount account = new UserAccount(principal, credentials);

        when(userAccountRepository.existsByPrincipal(principal)).thenReturn(true);
        when(userAccountRepository.findByPrincipal(principal)).thenReturn(Optional.of(account));
        when(userAccountRepository.findUserIdByPrincipal(principal)).thenReturn(0L);

        authenticationService.authenticate(principal, credentials);
        verify(tokenProvider).generateAccessToken(0L);
    }
}
