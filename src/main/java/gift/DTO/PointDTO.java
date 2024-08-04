package gift.DTO;

import java.beans.ConstructorProperties;

public class PointDTO {
    private int point;

    @ConstructorProperties({"point"})
    public PointDTO(int point){
        this.point = point;
    }

    public int getPoint() {
        return point;
    }
}
