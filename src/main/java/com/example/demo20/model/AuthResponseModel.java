package com.example.demo20.model;

public class AuthResponseModel {
    private String token;
    private String username;
    private String role;
    private String message; // thêm trường message

    public AuthResponseModel() {}

    // Constructor cho JWT + username + role
    public AuthResponseModel(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }

    // Constructor chỉ với message
    public AuthResponseModel(String message) {
        this.message = message;
    }

    // Getters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
}
