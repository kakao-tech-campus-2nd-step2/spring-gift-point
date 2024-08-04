package gift.member.util;

import gift.member.dto.MemberResponse;
import gift.member.entity.Member;
import gift.member.dto.MemberRequest;

public class MemberMapper {

    public static Member toEntity(MemberRequest memberRequest) {
        return new Member(
                memberRequest.email(),
                memberRequest.password()
        );
    }

    public static MemberResponse toResponseDto(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getPoint()
        );
    }

}
