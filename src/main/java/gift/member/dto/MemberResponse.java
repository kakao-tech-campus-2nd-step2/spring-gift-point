package gift.member.dto;

public record MemberResponse(
        Long id,
        String email,
        Integer point
) { }
