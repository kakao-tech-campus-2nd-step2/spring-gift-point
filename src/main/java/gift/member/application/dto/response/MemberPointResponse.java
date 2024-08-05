package gift.member.application.dto.response;

public record MemberPointResponse(
        Integer point
) {
    public static MemberPointResponse from(Integer point) {
        return new MemberPointResponse(point);
    }
}
