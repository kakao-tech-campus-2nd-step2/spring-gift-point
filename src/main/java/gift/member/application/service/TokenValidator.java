package gift.member.application.service;

import gift.exception.UnAuthorizationException;
import gift.member.persistence.LogoutTokenDao;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {
    private final LogoutTokenDao logoutTokenDao;

    public TokenValidator(LogoutTokenDao logoutTokenDao) {
        this.logoutTokenDao = logoutTokenDao;
    }

    public void validateToken(String token) throws UnAuthorizationException {
        if(!logoutTokenDao.findToken(token)) {
            throw new UnAuthorizationException("로그아웃되었습니다");
        }
    }
}
