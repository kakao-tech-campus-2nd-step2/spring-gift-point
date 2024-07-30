package gift.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    @Operation(summary = "홈페이지", description = "홈페이지로 이동한다.")
    public String home() {
        return "HomePage";
    }

}