package gift.model.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoMember {
    @JsonProperty("id")
    private Long Id;

    @JsonProperty("has_signed_up")
    private Boolean hasSignedUp;

    public KakaoMember(Boolean hasSignedUp) {
    }

    public KakaoMember(Long id, Boolean hasSignedUp) {
        Id = id;
        this.hasSignedUp = hasSignedUp;
    }

    public Long getId() {
        return Id;
    }

    public Boolean getHasSignedUp() {
        return hasSignedUp;
    }
}
