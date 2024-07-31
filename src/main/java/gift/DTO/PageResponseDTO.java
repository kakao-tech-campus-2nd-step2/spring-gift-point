package gift.DTO;

import java.util.List;

public class PageResponseDTO<T> {
    private List<T> content;
    private int number;
    private long totalElements;
    private int size;
    private boolean last;

    public PageResponseDTO(List<T> content, int number, long totalElements, int size, boolean last) {
        this.content = content;
        this.number = number;
        this.totalElements = totalElements;
        this.size = size;
        this.last = last;
    }

    public List<T> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }
}
