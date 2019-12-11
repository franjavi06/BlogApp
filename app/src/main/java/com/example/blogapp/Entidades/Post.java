package com.example.blogapp.Entidades;

public class Post implements Comparable<Post>{

    private Integer id;
    private String title;
    private String body;
    private long createdAt;

    private Integer userId;
    private String userName;
    private String userEmail;

    private Integer comments;
    private Boolean liked;
    private Integer likes;
    private String[] tags;
    private Integer views;


    public Post() {
    }

    public Post(Integer id, String title, String body, long createdAt, Integer userId, String userName, String userEmail, Integer comments, Boolean liked, Integer likes, String[] tags, Integer views) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.comments = comments;
        this.liked = liked;
        this.likes = likes;
        this.tags = tags;
        this.views = views;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void incrementLikes(){
        this.likes++;
    }

    public void decrementLikes(){
        this.likes--;
    }

    public void incrementComments(){
        this.comments++;
    }

    public void incrementViews(){
        this.views++;
    }

    @Override
    public int compareTo(Post o) {
        return (o.getId() < this.getId() ? -1 :
                (o.getId() == this.getId() ? 0 : 1));
    }
}
