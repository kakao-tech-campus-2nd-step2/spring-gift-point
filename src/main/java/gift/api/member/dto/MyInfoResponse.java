package gift.api.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.member.domain.Member;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MyInfoResponse(
    Long id,
    String email,
    Integer point
) {

    public static MyInfoResponse of(Member member) {
        return new MyInfoResponse(member.getId(), member.getEmail(), member.getPoint());
    }
}
