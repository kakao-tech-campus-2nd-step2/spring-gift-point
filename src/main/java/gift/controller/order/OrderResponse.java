package gift.controller.order;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record OrderResponse(UUID id, UUID optionId, Integer quantity, LocalDateTime orderDateTime,
                            String message) {

}
