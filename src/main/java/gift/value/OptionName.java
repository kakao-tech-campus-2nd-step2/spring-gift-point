package gift.value;

import java.util.regex.Pattern;

public class OptionName {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s]+$");
    private final String name;

    public OptionName(String name) {
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("옳지 않은 옵션명 입니다.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}