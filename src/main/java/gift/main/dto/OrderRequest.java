package gift.main.dto;

public record OrderRequest(
        long optionId,
        int quantity,
        String message
) {

}
