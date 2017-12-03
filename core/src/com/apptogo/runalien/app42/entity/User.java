package com.apptogo.runalien.app42.entity;

public class User {
    private String username;
    private String email;
    private String password;
    private UserStatus status;

    public User() {
        this.status = UserStatus.OFFLINE;
    }

    public User(String username, String password, String email) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public boolean isOnline() {
        return UserStatus.ONLINE.equals(this.status);
    }
}
