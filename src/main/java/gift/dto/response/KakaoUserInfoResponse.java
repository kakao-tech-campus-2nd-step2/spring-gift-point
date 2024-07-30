package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoResponse {
    
    @JsonProperty("id")
    private Long id;

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }
}
