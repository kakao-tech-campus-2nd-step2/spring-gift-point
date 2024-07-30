package gift.dto.request;

import gift.entity.Product;

public class MessageRequest {
    
    private String accessToken;
    private Product product;

    public MessageRequest(String accessToken, Product product){
        this.accessToken = accessToken;
        this.product = product;
    }
    
    public String getAccessToken(){
        return accessToken;
    }

    public Product getProduct(){
        return product;
    }
}
