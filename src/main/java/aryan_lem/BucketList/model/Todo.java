package aryan_lem.BucketList.model;

import java.io.Serializable;
import lombok.Data;


@Data
public class Todo implements Serializable{
    private String id;
    private String userId;
    private String title;
    private boolean completed=false;
}
