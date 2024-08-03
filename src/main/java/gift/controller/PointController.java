package gift.controller;

import gift.dto.PointDTO;
import gift.service.PointService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    public String addPoints(PointDTO pointDTO) {
        System.out.println(pointDTO);
        pointService.addPoint(pointDTO);
        return "redirect:/products";
    }
}
