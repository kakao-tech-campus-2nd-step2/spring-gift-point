package gift.dto;

public class KakaoUserProfile {

    private Long id;
    private KakaoAccount kakaoAccount;

    public Long getId() {
        return id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public static class KakaoAccount {
        private Profile profile;
        private String email;

        public Profile getProfile() {
            return profile;
        }

        public String getEmail() {
            return email;
        }

        public static class Profile {
            private String nickname;
            private String thumbnailImageUrl;

            public String getNickname() {
                return nickname;
            }

            public String getThumbnailImageUrl() {
                return thumbnailImageUrl;
            }
        }
    }
}

