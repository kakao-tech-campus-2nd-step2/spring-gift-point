package gift.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KakaoScopeResponse(List<Scope> scopes) {

    public record Scope(String id, @JsonProperty("display_name") String displayName, boolean agreed) {

    }
}
