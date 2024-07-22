package gift.member.dto;

import gift.member.domain.Email;
import gift.member.domain.MemberType;
import gift.member.domain.Nickname;
import gift.member.domain.Password;

import java.util.Objects;

public record MemberRequestDto(Email email, Password password, Nickname nickName) {
    public MemberRequestDto {
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);
        Objects.requireNonNull(nickName);
    }

    public MemberServiceDto toMemberServiceDto() {
        return new MemberServiceDto(null, MemberType.USER, email, password, nickName);
    }

    public MemberServiceDto toMemberServiceDto(Long id) {
        return new MemberServiceDto(id, MemberType.USER, email, password, nickName);
    }
}
