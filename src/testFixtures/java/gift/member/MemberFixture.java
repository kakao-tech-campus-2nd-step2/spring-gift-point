package gift.member;

import gift.member.dto.MemberReqDto;
import gift.member.entity.Member;

public class MemberFixture {

    public static Member createMember() {
        Member member = new Member("abc123@test.com", "1234");
        member.addPoints(10000);
        return member;
    }

    public static Member createMember(String email, String password) {
        return new Member(email, password);
    }

    public static MemberReqDto createMemberReqDto(String email, String password) {
        return new MemberReqDto(email, password);
    }
}
