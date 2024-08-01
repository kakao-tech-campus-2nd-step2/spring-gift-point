package gift.converter;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class StringToUrlConverter {

    private StringToUrlConverter() {
    }

    public static URL convert(String source) {
        try {
            return new URI(source).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("유효하지 않은 URL 형식입니다.");
        }
    }
}
