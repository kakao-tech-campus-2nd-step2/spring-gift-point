package gift.authentication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "cors")
public class CorsConfigurationProperties {

    private final String pathPatterns;
    private final String originPatterns;
    private final String allowedMethods;
    private final String allowedHeaders;
    private final String exposedHeaders;
    private final Boolean allowCredentials;
    private final Long maxAge;

    @ConstructorBinding
    public CorsConfigurationProperties(String pathPatterns, String originPatterns,
        String allowedMethods, String allowedHeaders, String exposedHeaders,
        Boolean allowCredentials,
        Long maxAge) {
        this.pathPatterns = pathPatterns;
        this.originPatterns = originPatterns;
        this.allowedMethods = allowedMethods;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
        this.allowCredentials = allowCredentials;
        this.maxAge = maxAge;
    }

    public String getPathPatterns() {
        return pathPatterns;
    }

    public String getOriginPatterns() {
        return originPatterns;
    }

    public String getAllowedMethods() {
        return allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public String getExposedHeaders() {
        return exposedHeaders;
    }

    public Boolean getAllowCredentials() {
        return allowCredentials;
    }

    public Long getMaxAge() {
        return maxAge;
    }
}
