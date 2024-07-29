package gift.dto;

import gift.vo.Member;

public record JoinMemberDto(
        String email,
        String password
){
    public Member toMember() {
        return new Member(email, password);
    }
}
