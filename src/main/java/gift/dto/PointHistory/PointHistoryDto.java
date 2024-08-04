package gift.dto.PointHistory;

import java.time.LocalDateTime;

public class PointHistoryDto {
    private Long userId;
    private int previousPoints;
    private int changePoints;
    private int currentPoints;

    public PointHistoryDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPreviousPoints() {
        return previousPoints;
    }

    public void setPreviousPoints(int previousPoints) {
        this.previousPoints = previousPoints;
    }

    public int getChangePoints() {
        return changePoints;
    }

    public void setChangePoints(int changePoints) {
        this.changePoints = changePoints;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public void setCurrentPoints(int currentPoints) {
        this.currentPoints = currentPoints;
    }
}
