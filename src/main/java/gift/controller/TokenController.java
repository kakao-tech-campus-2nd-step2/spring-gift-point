package gift.controller;

import gift.service.BasicTokenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/token")
@RestController
public class TokenController {
    private final BasicTokenService basicTokenService;

    public TokenController(BasicTokenService basicTokenService) {
        this.basicTokenService = basicTokenService;
    }

    @GetMapping("/{userId}")
    public String makeTokenFrom(@RequestParam("userId") Long userId) {
        return basicTokenService.makeTokenFrom(userId.toString());
    }

}
