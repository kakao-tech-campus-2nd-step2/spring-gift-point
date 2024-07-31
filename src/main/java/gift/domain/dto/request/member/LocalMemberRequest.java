package gift.domain.dto.request.member;

import gift.domain.entity.LocalMember;
import gift.domain.entity.Member;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import gift.global.util.HashUtil;
import jakarta.validation.constraints.NotNull;

public class LocalMemberRequest extends MemberRequest {

    @NotNull
    protected final String password;

    public LocalMemberRequest(String email, String password) {
        super(Type.LOCAL, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public LocalMember toEntity(Member member) {
        return new LocalMember(HashUtil.hashCode(password), member);
    }
}
