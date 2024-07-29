package gift.controller.order;

import java.util.UUID;

public record OrderRequest(UUID optionId, Integer quantity, String message) {

}