package gift.domain.model.dto;

import gift.domain.model.entity.User;

public class TokenRefreshDto {

    private final boolean refreshed;
    private final String newToken;
    private final User user;

    public TokenRefreshDto(boolean refreshed, String newToken, User user) {
        this.refreshed = refreshed;
        this.newToken = newToken;
        this.user = user;
    }

    public boolean isRefreshed() {
        return refreshed;
    }

    public String getNewToken() {
        return newToken;
    }

    public User getUser() {
        return user;
    }
}
