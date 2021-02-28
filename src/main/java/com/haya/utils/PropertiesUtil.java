package com.haya.utils;

import com.haya.rpc.Stub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author haya
 */
public class PropertiesUtil {
    public static Properties getDefaultConfigFile(){
        Properties pro = new Properties();
        InputStream ins = Stub.class.getClassLoader().getResourceAsStream( "application.properties" );

        try {
            pro.load( ins );
        } catch (IOException e) {
            throw new RuntimeException();
        }

        return pro;
    }
}
