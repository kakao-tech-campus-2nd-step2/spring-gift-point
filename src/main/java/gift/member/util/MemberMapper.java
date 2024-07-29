package gift.member.util;

import gift.member.entity.Member;
import gift.member.dto.MemberDto;

public class MemberMapper {

    public static Member toEntity(MemberDto memberDto) {
        return new Member(
                memberDto.email(),
                memberDto.password()
        );
    }

    public static MemberDto toDto(Member member) {
        return new MemberDto(
                member.getEmail(),
                member.getPassword()
        );
    }

}
