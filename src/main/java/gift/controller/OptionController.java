package gift.controller;

import gift.service.OptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/options")
@Tag(name = "Option API", description = "옵션 관련 API")
public class OptionController {

    @Autowired
    private OptionService optionService;


}
