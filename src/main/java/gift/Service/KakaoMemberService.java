package gift.Service;

import gift.DTO.KakaoJwtToken;
import gift.DTO.KakaoJwtTokenDto;
import gift.KakaoApi;
import gift.Repository.KakaoJwtTokenRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

@Service
public class KakaoMemberService {

  private static final String URL = "https://kauth.kakao.com/oauth/token";
  static Dotenv dotenv = Dotenv.configure().load();
  private static final String API_KEY = dotenv.get("API_KEY");
  private final KakaoJwtTokenRepository kakaoJwtTokenRepository;
  private final KakaoApi kakaoApi;

  public KakaoMemberService(KakaoJwtTokenRepository kakaoJwtTokenRepository, KakaoApi kakaoApi) {
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
    this.kakaoApi = kakaoApi;

  }

  public KakaoJwtTokenDto getToken(String autuhorizationKey) {
    KakaoJwtTokenDto kakaoJwtTokenDto = kakaoApi.kakaoLoginApiPost(URL, API_KEY, autuhorizationKey);
    KakaoJwtToken kakaoJwtToken = new KakaoJwtToken(kakaoJwtTokenDto.getAccessToken(),
      kakaoJwtTokenDto.getRefreshToken());
    kakaoJwtTokenRepository.save(kakaoJwtToken);

    return kakaoJwtTokenDto;

  }
}
