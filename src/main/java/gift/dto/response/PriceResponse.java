package gift.dto.response;

public class PriceResponse {
    private final int price;

    public PriceResponse(int price){
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }
}
