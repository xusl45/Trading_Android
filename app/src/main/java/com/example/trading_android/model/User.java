package com.example.trading_android.model;



public class User {

    private Integer id;


    private String username;
    private String password;
    private boolean state;
    private UserMessage userMessages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public UserMessage getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(UserMessage userMessages) {
        this.userMessages = userMessages;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", state=" + state +
                ", userMessages=" + userMessages +
                '}';
    }
}
