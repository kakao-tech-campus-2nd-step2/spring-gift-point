package gift.dto;

import java.util.ArrayList;
import java.util.List;

public class GetOptionDTO {
    private long product_Id;
    private List<String> optionList;

    public GetOptionDTO() {
        this.optionList = new ArrayList<>();
    }

    public GetOptionDTO(Long product_Id, List<String> optionList){
        this.product_Id = product_Id;
        this.optionList = optionList;
    }

    public long getProduct_Id() {
        return product_Id;
    }

    public List<String> GetOptionList() {
        return optionList;
    }

    @Override
    public String toString() {
        return "GetOptionDTO{" +
                "product_Id=" + product_Id +
                ", optionList=" + optionList +
                '}';
    }
}
