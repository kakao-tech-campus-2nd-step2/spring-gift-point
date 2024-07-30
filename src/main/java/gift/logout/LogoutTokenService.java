package gift.logout;

import org.springframework.stereotype.Service;

@Service
public class LogoutTokenService {

    private final LogoutTokenDao logoutTokenDao;

    public LogoutTokenService(LogoutTokenDao logoutTokenDao) {
        this.logoutTokenDao = logoutTokenDao;
    }

    public void postToken(String token) {
        logoutTokenDao.insertToken(token);
    }

    public boolean getToken(String token) {
        return logoutTokenDao.findToken(token);
    }

}
