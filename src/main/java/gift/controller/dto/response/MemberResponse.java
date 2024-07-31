package gift.controller.dto.response;

import gift.model.Member;
import gift.common.enums.Role;

public record MemberResponse(Long id, String email, Role role) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getRole());
    }
}
