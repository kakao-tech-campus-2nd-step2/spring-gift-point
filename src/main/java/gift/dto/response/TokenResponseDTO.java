package gift.dto.response;

import gift.entity.MemberType;

public record TokenResponseDTO(String token, MemberType memberType) {
}
