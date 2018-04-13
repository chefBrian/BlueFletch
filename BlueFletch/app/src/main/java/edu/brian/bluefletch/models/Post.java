package edu.brian.bluefletch.models;

public class Post {
    private final String postText;
    private final String createdDate;
    private final String username;
    private final String imageUrl;

    public Post(String postText, String createdDate, String username, String imageUrl){
        this.postText = postText;
        this.createdDate = createdDate;
        this.username  = username;
        this.imageUrl = imageUrl;
    }

    public String getPostText(){return postText;}
    public String getCreatedDate(){return createdDate;}
    public String getUsername(){return username;}
    public String getImageUrl(){return imageUrl;}
}
