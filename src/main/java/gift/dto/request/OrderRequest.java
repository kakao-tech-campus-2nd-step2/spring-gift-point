package gift.dto.request;

/*
optionId, optionQuantity, senderId, receiverId, hasCashReceipt:boolean,cashReceiptType?: 'PERSONAL' | 'BUSINESS';
  cashReceiptNumber?: string;, messageCardTextMessage: string;
 */
public record OrderRequest(
	Long optionId,
	Long quantity,
	String message
) {
}
