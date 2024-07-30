package gift.study;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.assertj.core.api.Assertions.assertThat;

public class MapStudyTest {
    @Test
    void putTest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "카카오 에러");
        map.put("name", "특수문자 에러");
        assertThat(map.size()).isEqualTo(1);
        assertThat(map.get("name")).isEqualTo("특수문자 에러");
    }
}