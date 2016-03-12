package com.kyanlife.code.evolis;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by kevinyan on 3/12/16.
 */
public class MainProperties {

    private static Properties properties = new Properties();

    static {
        try {
            properties.loadFromXML(MainProperties.class.getResourceAsStream("/main.properties.xml"));

            System.out.println(properties);
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private MainProperties () {

    }

    public static String get (String key) {
        return properties.getProperty(key);
    }

    public static Integer getInteger (String key) {
        Integer intProperty = null;
        if ( properties.containsKey(key) ) {
            try {
                intProperty = Integer.parseInt(
                        properties.getProperty(key)
                );
            } catch (Exception e) {

            }
        }
        return intProperty;
    }
}
