package gift.member.dto;

import gift.member.domain.Email;
import gift.member.domain.MemberType;
import gift.member.domain.Nickname;
import gift.member.domain.Password;

public record MemberRequestDto(Email email, Password password, Nickname nickname, Long point) {
    public MemberServiceDto toMemberServiceDto() {
        return new MemberServiceDto(null, MemberType.USER, email, password, nickname, point);
    }

    public MemberServiceDto toMemberServiceDto(Long id) {
        return new MemberServiceDto(id, MemberType.USER, email, password, nickname, point);
    }
}
