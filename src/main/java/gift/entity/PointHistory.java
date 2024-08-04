package gift.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.dto.PointHistory.PointHistoryDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_histories")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("previous_points")
    private int previousPoints;
    @JsonProperty("change_points")
    private int changePoints;
    @JsonProperty("current_points")
    private int currentPoints;
    @JsonProperty("change_date")
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
