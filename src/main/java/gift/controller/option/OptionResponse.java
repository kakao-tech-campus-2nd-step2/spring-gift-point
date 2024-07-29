package gift.controller.option;

import java.util.UUID;

public record OptionResponse(UUID id, String name, Integer quantity, UUID productId) {

}