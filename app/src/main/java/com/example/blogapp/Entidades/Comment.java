package com.example.blogapp.Entidades;

public class Comment implements Comparable<Comment>{

    private String body;
    private long createdAt;
    private Integer id;
    private Integer postId;
    private String userEmail;
    private Integer userId;
    private String userName;

    public Comment() {
    }

    public Comment(String body, long createdAt, Integer id, Integer postId, String userEmail, Integer userId, String userName) {
        this.body = body;
        this.createdAt = createdAt;
        this.id = id;
        this.postId = postId;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userName = userName;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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

    @Override
    public int compareTo(Comment o) {
        return (o.getId() < this.getId() ? -1 :
                (o.getId() == this.getId() ? 0 : 1));
    }
}
