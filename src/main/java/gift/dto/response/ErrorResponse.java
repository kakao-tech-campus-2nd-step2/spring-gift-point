package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonCreator;
=======
>>>>>>> 08d440e (refactor : 회원 API 명세서에 따른 리팩토링)

public class ErrorResponse {
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("error_code")
    private int code;

<<<<<<< HEAD
    @JsonCreator
    public ErrorResponse(
        @JsonProperty("message")
        String message, 

        @JsonProperty("error_code")
        int code){
=======
    public ErrorResponse(String message, int code){
>>>>>>> 08d440e (refactor : 회원 API 명세서에 따른 리팩토링)
        this.message = message;
        this.code = code;
    }

}
