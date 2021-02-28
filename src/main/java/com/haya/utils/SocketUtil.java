package com.haya.utils;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author haya
 */
public class SocketUtil {
    private static Socket socket;

    public static Socket getInstance() {
        if (socket == null) {
            synchronized (SocketUtil.class) {
                if (socket == null) {
                    Properties properties = PropertiesUtil.getDefaultConfigFile();
                    String host = properties.getProperty( "rpc.socket.host" );
                    int port = Integer.parseInt( properties.getProperty( "rpc.socket.port" ) );
                    int socketTimeOut = Integer.parseInt( properties.getProperty( "rpc.socket.time-out" ) );
                    long socketReconnectTimes = Long.parseLong( properties.getProperty( "rpc.socket.reconnect.times" ) );
                    long socketReconnectInterval = Long.parseLong( properties.getProperty( "rpc.socket.reconnect.interval" ) );
                    int i = 0;
                    do {
                        try {
                            System.out.println( "连接服务端" );
                            socket = new Socket( host, port );
                            socket.setSoTimeout( socketTimeOut );
                            System.out.println( "连接服务端成功" );
                            break;
                        } catch (IOException e) {
                            try {
                                System.err.println( "连接服务端失败" + i );
                                TimeUnit.MILLISECONDS.sleep( socketReconnectInterval );
                                System.err.println( "重新尝试连接" + i );
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    } while (i++ < socketReconnectTimes);
                }
            }
        }
        return socket;
    }
}
