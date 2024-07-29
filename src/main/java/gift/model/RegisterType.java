package gift.model;

public enum RegisterType {
    DEFAULT, KAKAO;

    public boolean isDefault() {
        return this == DEFAULT;
    }

    public boolean isKakao() {
        return this == KAKAO;
    }
}
