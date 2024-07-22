package gift.auth.dto;

import gift.member.domain.Email;
import gift.member.domain.Password;

public record LoginRequestDto(Email email, Password password) {

}
