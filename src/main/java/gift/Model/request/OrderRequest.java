package gift.Model.request;

public record OrderRequest(Long productId,
                           Long optionId,
                           Long quantity,
                           boolean hasCashReceipt,
                           String cashReceiptType,
                           String cashReceiptNumber,
                           String message,
                           Long point) {
}
