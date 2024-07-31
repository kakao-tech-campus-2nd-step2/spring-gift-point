package gift.domain.controller;

import gift.domain.controller.apiResponse.OauthTokenApiResponse;
import gift.domain.service.OauthService;
import gift.global.WebConfig.Constants.Domain.Member.Type;
import gift.global.apiResponse.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
public class OauthController {

    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/{userType}/redirect")
    public ResponseEntity<OauthTokenApiResponse> getToken(@PathVariable("userType") String userType, @RequestParam("code") String code) {
        return SuccessApiResponse.ok(new OauthTokenApiResponse(HttpStatus.OK, oauthService.getOauthToken(Type.from(userType), code)));
    }
}
