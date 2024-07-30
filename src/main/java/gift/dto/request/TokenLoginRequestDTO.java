package gift.dto.request;

import gift.entity.MemberType;

public record TokenLoginRequestDTO(Long memberId,
                                   String email,
                                   MemberType memberType,
                                   String token) {
}

