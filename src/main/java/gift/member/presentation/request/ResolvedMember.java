package gift.member.presentation.request;

public record ResolvedMember(
        Long id
) {
    public static ResolvedMember from (Long id) {
        return new ResolvedMember(id);
    }
}
