package com.haya.rpc;

import com.haya.utils.PropertiesUtil;
import com.haya.utils.SocketUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
public class Stub {
    private static final int RETRY_TIMES;
    private static final int INTERVAL;
    static {
        Properties configFile = PropertiesUtil.getDefaultConfigFile();
        RETRY_TIMES = Integer.parseInt( configFile.getProperty( "rpc.request.retry.times" ) );
        INTERVAL = Integer.parseInt( configFile.getProperty( "rpc.request.retry.interval" ) );
    }
    static Object getStub(Class c) {
        InvocationHandler h = (proxy, method, args) -> {
            Socket socket = SocketUtil.getInstance();
            Object result = null;
            int i = 0;
            do {
                try {
                    ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );
                    oos.writeUTF( c.getName() );
                    oos.writeUTF( method.getName() );
                    oos.writeObject( method.getParameterTypes() );
                    oos.writeObject( args );
                    oos.flush();
                    ObjectInputStream ois = new ObjectInputStream( socket.getInputStream() );
                    result = ois.readObject();
                    return result;
                } catch (IOException e) {
                    TimeUnit.MILLISECONDS.sleep( INTERVAL );
                    System.out.println( "重新发送请求" + i );
                }
            } while (i++ < RETRY_TIMES);
            return null;
        };
        // 生成代理对象
        return Proxy.newProxyInstance( c.getClassLoader(), new Class[]{c}, h );
    }
}
