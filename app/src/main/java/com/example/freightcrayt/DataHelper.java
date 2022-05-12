package com.example.freightcrayt;

public class DataHelper {

    // Singleton instance for holding persistent data in prototype
    private static DataHelper singleton_instance = null;


    // static method for getting single instance
    public static DataHelper getInstance()
    {
        if (singleton_instance == null)
            singleton_instance = new DataHelper();

        return singleton_instance;
    }
}
