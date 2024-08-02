package gift.dto;

import gift.domain.Member;

public class MemberPointDto {
    private final int point;

    public MemberPointDto(Member member) {
        this.point = member.getPoint();
    }
    public int getPoint() {
        return point;
    }
}
