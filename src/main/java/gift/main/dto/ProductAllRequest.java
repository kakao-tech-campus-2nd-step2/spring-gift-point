package gift.main.dto;

import java.util.List;

public record ProductAllRequest(
        String name,
        int price,
        String imageUrl,
        int categoryUniNumber,
        List<OptionRequest> optionRequests) {

    public ProductAllRequest(ProductRequest productRequest, OptionListRequest optionListRequest) {
        this(productRequest.name(), productRequest.price(), productRequest.imageUrl(), productRequest.categoryUniNumber(), optionListRequest.optionRequests());
    }

    @Override
    public String toString() {
        return "ProductAllRequest{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", categoryUniNumber=" + categoryUniNumber +
                ", optionRequests=" + optionRequests +
                '}';
    }

}
