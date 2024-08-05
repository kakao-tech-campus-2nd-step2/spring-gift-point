package gift.member.service;

import gift.member.model.Member;

public interface MemberService {

    void registerMember(String email, String password);

    String login(String email, String password);

    Long getPoint(String memberEmail);

    Member getMemberByToken(String token);
}
