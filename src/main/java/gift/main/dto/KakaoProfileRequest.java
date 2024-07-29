package gift.main.dto;

public record KakaoProfileRequest(Long id, Properties properties) {

    public String nickname() {
        return properties().nickname;
    }

    public String email() {
        return id.toString() + properties().nickname;
    }

    record Properties(String nickname) {
    }

}
