package gift.service;

import gift.dto.LoginMemberToken;
import gift.dto.MemberRequest;

public interface MemberService {

    void register(MemberRequest memberRequest);

    LoginMemberToken login(MemberRequest memberRequest);

    boolean checkRole(MemberRequest memberRequest);

    MemberRequest getLoginUser(String token);
}
