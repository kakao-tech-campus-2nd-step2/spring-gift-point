package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for User Point")
public class UserPointRequest {

    @Schema(description = "User Point", example = "10000")
    private Integer point;

    public UserPointRequest() {
    }

    public UserPointRequest(Integer point) {
        this.point = point;
    }

    public Integer getPoint() {
        return point;
    }
}
