package com.sarah.tourbookingsystem.model;

abstract class User {
    protected String username;
    protected String password;

    public User(String var1, String var2) {
        this.username = var1;
        this.password = var2;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean checkPassword(String var1) {
        return this.password.equals(var1);
    }
}