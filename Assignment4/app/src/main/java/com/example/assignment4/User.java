package com.example.assignment4;

import java.io.Serializable;

public class User implements Serializable
{

    private String name;
    private String email;
    private Role role;

    public User(String name, String email, Role role)
    {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }

}

