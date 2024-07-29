package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {
    
    @JsonProperty("result_code")
    private int code;

    public void setCode(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
