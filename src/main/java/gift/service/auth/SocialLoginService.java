package gift.service.auth;

import gift.util.api.ApiCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialLoginService {
    private final ApiCall apiCall;

    @Autowired
    public SocialLoginService(ApiCall apiCall) {
        this.apiCall = apiCall;
    }

    public String getKakaoCode(){
        return apiCall.getKakaoCode();
    }
}
