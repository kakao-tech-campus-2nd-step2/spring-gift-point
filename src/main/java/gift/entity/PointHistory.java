package gift.entity;

import gift.dto.PointHistory.PointHistoryDto;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private int previousPoints;
    private int changePoints;
    private int currentPoints;
    private LocalDateTime changeDate;

    public PointHistory(PointHistoryDto pointHistoryDto) {
        this.userId = pointHistoryDto.getUserId();
        this.previousPoints = pointHistoryDto.getPreviousPoints();
        this.changePoints = pointHistoryDto.getChangePoints();
        this.currentPoints = pointHistoryDto.getCurrentPoints();
        this.changeDate = LocalDateTime.now();
    }

    public PointHistory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }
}
