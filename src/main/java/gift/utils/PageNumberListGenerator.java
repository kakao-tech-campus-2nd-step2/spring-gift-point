package gift.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;

public class PageNumberListGenerator {

    /**
     * Page 객체를 통해 1부터 페이지 번호가 저장된 리스트를 반환
     *
     * @param page
     * @return List
     */
    public static List<Integer> generatePageNumberList(Page<?> page) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages).boxed().toList();
        }
        return Collections.emptyList();
    }
}
