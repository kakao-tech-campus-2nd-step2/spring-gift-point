package gift.product;

import gift.option.Option;
import gift.option.OptionResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int price;

    private String imageUrl;

    private Long categoryId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private final List<Option> options = new ArrayList<>();

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getPrice(){
        return this.price;
    }

    public String getImageUrl(){
        return this.imageUrl;
    }

    public Long getCategoryId(){
        return this.categoryId;
    }

    public List<Option> getOptions() {
        return this.options;
    }


    protected Product(){
    }
    public Product(String name, int price, String imageUrl, Long categoryId){
        isValidName(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Product(Long id, String name, int price, String imageUrl, Long categoryId){
        this.id = id;
        isValidName(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public void update(String name, int price, String imageUrl, Long categoryId){
        isValidName(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    private void isValidName(String name){
        if(name.contains("카카오")){
            throw new IllegalArgumentException( "\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }
    }

    public List<OptionResponse> getOptionResponses(){
        List<OptionResponse> optionResponseList = new ArrayList<>();
        for (Option e : this.getOptions()){
            optionResponseList.add(new OptionResponse(e));
        }
        return optionResponseList;
    }

}
