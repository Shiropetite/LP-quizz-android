package fr.android.quizz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String username;
    private String avatar;
    private int score;
    private ArrayList<User> friends;

    public User() {
        this.friends = new ArrayList<User>();
        this.score = 0;
    }

    public User(String id, String username, String avatar, int score) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.score = score;
        this.friends = new ArrayList<User>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", score='" + score + '\'' +
                ", friends=" + friends +
                '}';
    }
}
