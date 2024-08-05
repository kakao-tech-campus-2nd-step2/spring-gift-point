package gift.domain.member.dto;

import gift.domain.member.entity.Member;

public record PointResponse(
    int amount
) {
    public static PointResponse from(Member member) {
        return new PointResponse(member.getPoint());
    }
}
