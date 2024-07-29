package gift.dto;

import gift.vo.Member;

public record LoginDto(
        String email,
        String password
) {
    public Member toUser() {
        return new Member(email, password);
    }
}
