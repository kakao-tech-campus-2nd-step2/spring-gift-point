package gift.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/categories")
@RestController
@Tag(name = "Category Management", description = "Category Management API")
public class CategoryController {

}
