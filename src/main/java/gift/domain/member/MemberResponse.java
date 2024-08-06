package gift.domain.member;

public record MemberResponse(
    Long id,
    MemberType memberType
) {

    public MemberResponse(Member member) {
        this(member.getId(), member.getMemberType());
    }
}
