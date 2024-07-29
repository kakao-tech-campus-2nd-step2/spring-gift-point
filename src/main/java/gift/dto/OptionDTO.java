package gift.dto;

import java.util.List;

public class OptionDTO {
    private long id;
    private List<String> optionList;
    private long productID;

    public OptionDTO(String options, long productID){
        optionList = List.of(options.split(","));
        this.productID = productID;
    }

    public OptionDTO(Long id, List<String> option, long productID){
        this.id = id;
        this.optionList = option;
        this.productID = productID;
    }

    public List<String> getOptionList() {
        return optionList;
    }

    public long getProductID() {
        return productID;
    }
}
