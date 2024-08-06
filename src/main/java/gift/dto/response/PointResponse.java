package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PointResponse {
    
    private int point;

    public PointResponse(int point){
        this.point = point;
    }

    public int getPoint(){
        return point;
    }
}
