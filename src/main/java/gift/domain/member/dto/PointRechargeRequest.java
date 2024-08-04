package gift.domain.member.dto;

import org.hibernate.validator.constraints.Range;

public record PointRechargeRequest(

    @Range(min = 1, max = 1000000)
    int amount
) {

}
