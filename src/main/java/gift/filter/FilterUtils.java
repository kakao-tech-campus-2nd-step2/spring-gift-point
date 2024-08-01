package gift.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static gift.utils.FilterConstant.*;

public class FilterUtils {

    public static final String CODE = "401";
    public static final String MESSAGE = "인증되지 않은 사용자 입니다.";

    public static void sendErrorResponse(HttpServletResponse response, int statusCode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("code", CODE);
        errorResponse.put("message", MESSAGE);

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

    public static void setCorsHeader(HttpServletResponse httpResponse){
        httpResponse.setHeader("Access-Control-Allow-Origin", "*"); // 모든 도메인 허용
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }

    public static boolean checkOptionMethod(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        return false;
    }

    public static boolean isUrlInWhiteList(String path){
        return path.equals(HOME_URL)
                || path.startsWith(CATEGORY_URL)
                || path.startsWith(PRODUCT_URL)
                || path.equals(KAKAO_TOKEN_RENEW_URL)
                || path.startsWith(LOGIN_URL_PREFIX)
                || path.startsWith(LOGIN_OAUTH_URL_PREFIX)
                || path.startsWith(H2_DB_URL)
                || path.equals(SWAGGER_UI_HTML)
                || path.startsWith(SWAGGER_UI)
                || path.startsWith(API_DOCS)
                || path.startsWith(V3_API_DOCS)
                || path.startsWith(SWAGGER_RESOURCES);
    }
}
