package com.example.appmenu;

import java.io.Serializable;

public class UsuarioLogin implements Serializable {

    private String username;
    private String password;

    public UsuarioLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}