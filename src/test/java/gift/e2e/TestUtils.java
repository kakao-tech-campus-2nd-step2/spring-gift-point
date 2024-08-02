//package gift.e2e;
//
//import gift.DTO.MemberDTO;
//import gift.auth.DTO.TokenDTO;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//
//public class TestUtils {
//
//    public static String signup(TestRestTemplate restTemplate, String baseUrl, String email,
//        String password) {
//        String url = baseUrl + "/api/login/signup";
//        MemberDTO member = new MemberDTO(email, password);
//        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(url, member, TokenDTO.class);
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
////            System.out.println(response.getBody().token());
//            return response.getBody().token();
//        }
//        return null;
//    }
//
//    public static String login(TestRestTemplate restTemplate, String baseUrl, String email,
//        String password) {
//        String url = baseUrl + "/api/login";
//        MemberDTO member = new MemberDTO(email, password);
//        ResponseEntity<TokenDTO> response = restTemplate.postForEntity(url, member, TokenDTO.class);
//        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
////            System.out.println(response.getBody().token());
//            return response.getBody().token();
//        }
//        throw new RuntimeException("Login failed");
//    }
//
//    public static <T> HttpEntity<T> createRequestEntity(T body, String authToken) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json");
//        if (authToken != null && !authToken.isEmpty()) {
//            headers.set("Authorization", "Bearer " + authToken);
//        }
//        return new HttpEntity<>(body, headers);
//    }
//
//    public static <T> ResponseEntity<T> sendRequest(TestRestTemplate restTemplate, String url,
//        HttpMethod method, Object body, String authToken, Class<T> responseType) {
//        HttpEntity<?> requestEntity = createRequestEntity(body, authToken);
//        return restTemplate.exchange(url, method, requestEntity, responseType);
//    }
//
//    public static <T> ResponseEntity<T> sendRequest(TestRestTemplate restTemplate, String url,
//        HttpMethod method, Object body, String authToken,
//        ParameterizedTypeReference<T> responseType) {
//        HttpEntity<?> requestEntity = createRequestEntity(body, authToken);
//        return restTemplate.exchange(url, method, requestEntity, responseType);
//    }
//
//    public static <T> ResponseEntity<T> sendRequest(TestRestTemplate restTemplate, String url,
//        HttpMethod method, Object body, String authToken, Class<T> responseType,
//        String... queryParams) {
//        String fullUrl = buildUrlWithQueryParams(url, queryParams);
//        HttpEntity<?> requestEntity = createRequestEntity(body, authToken);
//        return restTemplate.exchange(fullUrl, method, requestEntity, responseType);
//    }
//
//    public static <T> ResponseEntity<T> sendRequest(TestRestTemplate restTemplate, String url,
//        HttpMethod method, Object body, String authToken,
//        ParameterizedTypeReference<T> responseType, String... queryParams) {
//        String fullUrl = buildUrlWithQueryParams(url, queryParams);
//        HttpEntity<?> requestEntity = createRequestEntity(body, authToken);
//        return restTemplate.exchange(fullUrl, method, requestEntity, responseType);
//    }
//
//    private static String buildUrlWithQueryParams(String url, String... queryParams) {
//        if (queryParams.length > 0) {
//            return url + "?" + String.join("&", queryParams);
//        }
//        return url;
//    }
//}