package gift.dto.request;

import gift.entity.MemberType;

public record KakaoMemberRequestDTO(String email, MemberType memberType) {
}
