package gift.controller.rest;

import gift.dto.PointHistory.PointHistoryResponseDto;
import gift.repository.PointHistoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pointHistories")
public class PointHistoryController {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryController(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PointHistoryResponseDto>> getPointHistory(@PathVariable Long userId) {
        List<PointHistoryResponseDto> result = pointHistoryRepository.findPointHistoriesByUserId(userId)
                .stream()
                .map(pointHistory -> new PointHistoryResponseDto(pointHistory))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(result);
    }
}
