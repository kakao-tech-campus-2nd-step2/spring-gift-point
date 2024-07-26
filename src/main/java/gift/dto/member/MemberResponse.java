package gift.dto.member;

import gift.model.RegisterType;

public record MemberResponse(
    Long id,
    String email,
    String token,
    RegisterType registerType
) {

}
