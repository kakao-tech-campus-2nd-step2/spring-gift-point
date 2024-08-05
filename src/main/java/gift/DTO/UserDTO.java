package gift.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class UserDTO {
//    private Long id;
    private String email;
    private String password;
//    private List<WishDTO> wishes;

    public UserDTO() {}

//    public UserDTO(Long id, String email, String password, List<WishDTO> wishes) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.wishes = wishes;
//    }

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public List<WishDTO> getWishes() {
//        return wishes;
//    }
//
//    public void setWishes(List<WishDTO> wishes) {
//        this.wishes = wishes;
//    }
}
