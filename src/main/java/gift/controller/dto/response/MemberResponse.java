package gift.controller.dto.response;

import gift.model.Member;
import gift.common.enums.Role;

public record MemberResponse(Long id, String email, String name, Role role, int point) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getRole(), member.getPoint());
    }
}
