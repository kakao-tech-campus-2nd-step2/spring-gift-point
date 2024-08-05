package gift.response.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.model.Member;

public record MemberInfoResponse(
    String email,
    @JsonProperty(value = "remain_point")
    Integer remainPoint
) {

    private MemberInfoResponse(Member member) {
        this(member.getEmail(), member.getPoint());
    }

    public static MemberInfoResponse createMemberInfo(Member member) {
        return new MemberInfoResponse(member);
    }
}
