package gift.controller.member;

import gift.controller.auth.LoginRequest;
import gift.domain.Member;

public class MemberMapper {

    public static Member from(SignUpRequest member) {
        return new Member(member.getEmail(), member.getPassword(), member.getNickName());
    }

    public static MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(),
            member.getNickName(), member.getGrade());
    }
}