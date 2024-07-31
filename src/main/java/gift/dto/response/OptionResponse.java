package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.OptionDto;

public class OptionResponse {
    
    private OptionDto optionDto;

    public OptionResponse(
        @JsonProperty("option")
        OptionDto optionDto
    ){
        this.optionDto = optionDto;
    }

    public OptionDto getOptionDto(){
        return optionDto;
    }
}
