package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("error_code")
    private int code;

    public ErrorResponse(String message, int code){
        this.message = message;
        this.code = code;
    }

}
