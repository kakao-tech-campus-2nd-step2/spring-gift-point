package gift.dto.response;

import gift.domain.Member;

public record MemberResponse(Long id, String email, String password, int points) {
    public static MemberResponse from(final Member member){
        return new MemberResponse(member.getId(), member.getEmail(), member.getPassword(), member.getPoints());
    }
}
