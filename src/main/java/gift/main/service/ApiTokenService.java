package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.config.KakaoProperties;
import gift.main.dto.KakaoToken;
import gift.main.dto.UserVo;
import gift.main.entity.ApiToken;
import gift.main.entity.User;
import gift.main.repository.ApiTokenRepository;
import gift.main.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

import static io.jsonwebtoken.lang.Strings.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class ApiTokenService {

    private static final MediaType FORM_URLENCODED = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);

    private final ApiTokenRepository apiTokenRepository;
    private final UserRepository userRepository;
    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public ApiTokenService(ApiTokenRepository apiTokenRepository, UserRepository userRepository, KakaoProperties kakaoProperties) {
        this.apiTokenRepository = apiTokenRepository;
        this.userRepository = userRepository;
        this.kakaoProperties = kakaoProperties;
        restClient = RestClient.create();
    }

    //토큰저장
    @Transactional
    public void saveToken(User user, KakaoToken kakaoToken) {
        if(apiTokenRepository.existsByUserId(user.getId())){
            ApiToken apiToken = apiTokenRepository.findByUserId(user.getId()).get();
            apiToken.updete(kakaoToken);
        }

        ApiToken apiToken = new ApiToken(user, kakaoToken);
        apiTokenRepository.save(apiToken);

    }

    //토큰갱신
    public ApiToken renewToken(UserVo userVo) {
        ApiToken apiToken = apiTokenRepository.findByUserId(userVo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TOKEN));

        if (apiToken.getAccessTokenExpirationDate().isAfter(LocalDateTime.now())) {
            return apiToken;
        }

        //지금보다 이후
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", kakaoProperties.refreshToken());
        map.add("client_id", kakaoProperties.clientId());
        map.add("refresh_token", apiToken.getRefreshToken());

        KakaoToken renewToken = restClient.post()
                .uri(kakaoProperties.tokenRenewalRequestUri())
                .contentType(FORM_URLENCODED)
                .body(map)
                .retrieve()
                .toEntity(KakaoToken.class)
                .getBody();

        assert renewToken != null;
        apiToken.updete(renewToken);
        apiTokenRepository.save(apiToken);

        return apiToken;
    }

}
