package gift.global.util;

import java.io.InputStream;
import java.util.Scanner;

public final class StringUtils {

    public static String convert(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        return scanner.useDelimiter("\\A").next();
    }
}
