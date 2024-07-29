package gift.product;

public record ProblemDetailResponse(
    String type,
    String title,
    int status,
    String detail,
    String instance
) {

}
