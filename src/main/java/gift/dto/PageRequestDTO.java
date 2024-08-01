package gift.dto;

public record PageRequestDTO(
    int page,
    int size,
    String sort
) {

}
