package gift.controller.category;

import java.util.UUID;

public record CategoryResponse(UUID id, String name, String color, String description, String imageUrl) {

}