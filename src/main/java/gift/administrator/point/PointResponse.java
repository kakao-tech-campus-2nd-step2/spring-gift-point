package gift.administrator.point;

public class PointResponse {

    private Long userId;
    private int points;

    public PointResponse(){

    }

    public PointResponse(Long userId, int points){
        this.userId = userId;
        this.points = points;
    }

    public Long getUserId(){
        return userId;
    }

    public int getPoints(){
        return points;
    }
}
