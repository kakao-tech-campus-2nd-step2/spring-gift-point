package testFixtures;

import gift.member.entity.Member;

public class MemberFixture {

    public static Member createMember(String email) {
        return new Member(email, "password");
    }

}
