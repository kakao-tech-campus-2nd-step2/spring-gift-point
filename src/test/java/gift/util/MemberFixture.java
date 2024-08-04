package gift.util;

import gift.member.entity.Member;

public class MemberFixture {

    public static Member createMember() {
        return createMember("admin");
    }

    public static Member createMember(String password) {
        return new Member("admin@gmail.com", password, 0);
    }
}
