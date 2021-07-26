package com.exemplo.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConverterEnvToProperty {

    private ConverterEnvToProperty() { }

    public static Properties fromConverterEnv(){
        var properties = new Properties();
        readEnviroment(properties);
        return properties;
    }

    public static Properties fromConverterEnv(InputStream in){
        var properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readEnviroment(properties);
        return properties;
    }


    private static void readEnviroment(Properties properties) {
        var env = Dotenv.load();

        for (DotenvEntry e : env.entries()) {
            properties.forEach((key, value) -> {
                if(value.toString().contains(e.getKey())){
                    properties.setProperty(key.toString(), value.toString().replace("${" +  e.getKey() + "}", e.getValue()));
                } else {
                    properties.setProperty(e.getKey(), e.getValue());
                }
            });
        }
    }
}
