package gift.wishedProduct.dto;

public record GetWishedProductResponse(
    long productId,
    String name,
    int price,
    String imageUrl,
    long wishId
) {

}
