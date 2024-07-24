package gift.dto.oauth;

import java.util.List;

public record KakaoScopeResponse(List<Scope> scopes) {

    public record Scope(String id, String displayName, boolean agreed) {

    }
}
