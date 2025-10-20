package aryan_lem.BucketList.model;

import java.io.Serializable;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private String id;
    private String username;
    private String passwordHash;
}
