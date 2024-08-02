package gift.dto.option;

import lombok.Data;
import lombok.Getter;

@Data
public class DeleteOptionRequest {
    private String email;
    private String password;
}
