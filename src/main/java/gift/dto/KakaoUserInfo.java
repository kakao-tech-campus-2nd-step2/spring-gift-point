package gift.dto;

public class KakaoUserInfo {

    private Long id;
    private String nickName;
    private String profileImageUrl;
    private String email;

    public KakaoUserInfo() {
    }

    public KakaoUserInfo(Long id, String nickName, String profileImageUrl, String email) {
        this.id = id;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getEmail() {
        return email;
    }

}
