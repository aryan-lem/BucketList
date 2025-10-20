package aryan_lem.BucketList.dto;

import lombok.Getter;

@Getter
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;

    public UserDTO() {}

    public UserDTO(String username,String password,String email, String id){
        this.username=username;
        this.password=password;
        this.email=email;
        this.id=id;
    }

    public void setUsername(String username){this.username=username;}

    public void setPassword(String password){this.password=password;}

    public void setEmail(String email){this.email=email;}

    public void setId(String id){this.id=id;}
}
