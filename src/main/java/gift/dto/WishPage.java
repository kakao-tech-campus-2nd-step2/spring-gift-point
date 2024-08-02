package gift.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishPage {
    private int totalPage;

    private List<?> content ; ;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<?> getContent() {
        return content;
    }

    public void setContent(List<?> content) {
        this.content = content;
    }
}
