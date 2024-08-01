package gift.dto.request;

import gift.domain.Member;

public record MemberRequest(Long id, String email, String password, int points) {
    public Member toEntity(){
        return new Member(this.id, this.email, this.password, this.points);
    }
}
