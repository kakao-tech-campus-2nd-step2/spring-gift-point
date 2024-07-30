package gift.global.exception.option;

public class OptionOnlyOneDeleteException extends RuntimeException{

    public OptionOnlyOneDeleteException(Long optionId) {
        super("해당 상품의 옵션의 개수가 1개이기 때문에 옵션(ID:" + optionId +")을 삭제할 수 없습니다.");
    }
}
