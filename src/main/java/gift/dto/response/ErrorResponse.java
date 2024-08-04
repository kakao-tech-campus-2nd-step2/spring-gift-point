package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

public class ErrorResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("error_code")
    private int code;

    @JsonCreator
    public ErrorResponse(
        @JsonProperty("message")
        String message, 

        @JsonProperty("error_code")
        int code){
        this.message = message;
        this.code = code;
    }

}
