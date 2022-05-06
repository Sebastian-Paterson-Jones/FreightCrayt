package com.example.freightcrayt;

public class DataHelper {

    // Singleton instance for holding persistent data in prototype
    private static DataHelper singleton_instance = null;

    // Global variables for application
    private String email;
    private String password;
    private String name;
    private String surname;

    // Getters
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }
    public String getName() {
        return this.name;
    }
    public String getSurname() {
        return this.surname;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }

    // static method for getting single instance
    public static DataHelper getInstance()
    {
        if (singleton_instance == null)
            singleton_instance = new DataHelper();

        return singleton_instance;
    }
}
