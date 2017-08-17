package ru.startandroid.freeadvertising.model;



public class Product {
    private long id;
    private String name;
    private long type;
    private String score;
    private String comment;
    private String imagepath;
    private String user_login;

    public Product(String name, String score, String comment, String imagepath, long type, String user_login) {
        this.name = name;
        this.type = type;
        this.score = score;
        this.comment = comment;
        this.imagepath = imagepath;
        this.user_login = user_login;
    }

    public long getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public long getType() {
        return type;
    }

    public String getComment() {
        return comment;
    }

    public String getImagepath() {
        return imagepath;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUser_login() {
        return user_login;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setUser_login(String user_login) {
        this.user_login = user_login;
    }
}
