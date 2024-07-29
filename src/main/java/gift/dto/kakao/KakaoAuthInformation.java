package gift.dto.kakao;

public record KakaoAuthInformation(String name, String email) {
    public static KakaoAuthInformation of(String name, String email) {
        return new KakaoAuthInformation(name, email);
    }
}
