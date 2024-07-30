package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Schema(description = "api 연동시 필요한 정보")
public class Properties {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "ai 연동 정보 고유 id")
    private Long id;
    @Schema(description = "클라이언트의 rest API 앱키")
    private final String clientId;
    @Schema(description = "외부 api 응답 정보를 회신할 uri")
    private final String redirectUri;

    public Properties() {
        this("5c345cd75534a877a4c4e77e6e7bd288", "http://localhost:8080");
    }

    public Properties(String clientId, String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

}
