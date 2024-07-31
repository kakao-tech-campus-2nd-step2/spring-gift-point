package gift.domain.dto.request;

import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain.Member.Permission;

public record MemberPermissionChangeRequest(String email, Permission permission) {

    public static MemberPermissionChangeRequest of(Member member) {
        return new MemberPermissionChangeRequest(member.getEmail(), member.getPermission());
    }
}
