package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "kakao_member")
public class KakaoMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "ID는 1글자 이상 입력하세요")
    @Column(unique = true, nullable = false)
    private String uniqueId;

    @NotBlank
    @Column(nullable = false)
    private String accessToken;

    protected KakaoMember() {}

    public KakaoMember(Long id, String uniqueId, String accessToken) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.accessToken = accessToken;
    }

    public static KakaoMember createWithKakaoAccessToken(String uniqueId, String accessToken) {
        return new KakaoMember(null, uniqueId, accessToken);
    }

    public KakaoMember updateKakaoAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public Long getId() {
        return id;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
