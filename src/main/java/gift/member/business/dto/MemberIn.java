package gift.member.business.dto;

import gift.global.domain.OAuthProvider;
import gift.member.persistence.entity.Member;

public class MemberIn {

    public record Login(
        String email,
        String password
    ) {

    }

    public record Register(
        String email,
        String password
    ) {

        public Member toMember() {
            return new Member(email, password);
        }
    }

    public record VendorRegister(
        String email,
        OAuthProvider oAuthProvider,
        String accessToken,
        String refreshToken
    ) {

        public Member toMember() {
            return new Member(email, oAuthProvider, accessToken, refreshToken);
        }
    }

    public record VendorLogin(
        String email,
        OAuthProvider oAuthProvider,
        String accessToken,
        String refreshToken
    ) {

    }
}
