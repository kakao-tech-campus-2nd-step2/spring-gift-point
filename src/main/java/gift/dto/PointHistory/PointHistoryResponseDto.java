package gift.dto.PointHistory;

import gift.entity.PointHistory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class PointHistoryResponseDto {
    private Long id;
    private int previousPoints;
    private int changePoints;
    private int currentPoints;
    private LocalDateTime changeDate;

    public PointHistoryResponseDto(PointHistory pointHistory) {
        this.id = pointHistory.getId();
        this.previousPoints = pointHistory.getPreviousPoints();
        this.changePoints = pointHistory.getChangePoints();
        this.currentPoints = pointHistory.getCurrentPoints();
        this.changeDate = pointHistory.getChangeDate();
    }

    public PointHistoryResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }
}
