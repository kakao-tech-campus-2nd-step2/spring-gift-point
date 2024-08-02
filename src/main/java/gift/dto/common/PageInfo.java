package gift.dto.common;

public record PageInfo(
    int currentPage,
    long totalData,
    int totalPages) {
}