package gift.dto;

import gift.domain.KakaoAccount;

public class KakaoResponse {
    private long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getConnected_at() {
        return connected_at;
    }

    public void setConnected_at(String connected_at) {
        this.connected_at = connected_at;
    }

    public KakaoAccount getKakao_account() {
        return kakao_account;
    }

    public void setKakao_account(KakaoAccount kakao_account) {
        this.kakao_account = kakao_account;
    }
}
