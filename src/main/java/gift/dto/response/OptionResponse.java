package gift.dto.response;

import java.util.List;

import gift.dto.OptionDto;

public class OptionResponse {
    
    private List<OptionDto> options;

    public OptionResponse(List<OptionDto> options){
        this.options = options;
    }

    public List<OptionDto> getOptions(){
        return options;
    }
}
