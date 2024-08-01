package gift.dto.response;

public record CategoryResponseDTO(Long Id,
                                  String name,
                                  String color,
                                  String imageUrl,
                                  String description
                                  ) {
}
