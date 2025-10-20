package aryan_lem.BucketList.dto;

public class TodoRequest {
    private String title;
    private String description;

    public TodoRequest(){}

    public TodoRequest(String title,String description){
        this.title=title;
        this.description=description;
    }

    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}

    public String getDescription(){return description;}
    public void setDescription(String description){this.description=description;}
}
