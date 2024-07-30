package gift.dto.response;

import java.util.List;

public record CategoryResponseDTO(Long CategoryId,
                                  String name,
                                  String color,
                                  String description,
                                  String imageUrl) {
}
