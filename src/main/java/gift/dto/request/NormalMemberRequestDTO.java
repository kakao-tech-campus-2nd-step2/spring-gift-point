package gift.dto.request;

import gift.entity.MemberType;

public record NormalMemberRequestDTO(String email,
                                     String password,
                                     MemberType memberType) {
}
