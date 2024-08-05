package gift.repository.fixture;

import gift.domain.Member;

public class MemberFixture {
    public static Member createMember(String email,String password){
        return new Member(email,password);
    }
}
