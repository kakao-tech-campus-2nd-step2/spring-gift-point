package gift.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.OptionDto;

public class GetOptionsResponse {
    
    private List<OptionDto> options;

    @JsonCreator
    public GetOptionsResponse(
        @JsonProperty("options")
        List<OptionDto> options){
        this.options = options;
    }

    public List<OptionDto> getOptions(){
        return options;
    }
}
