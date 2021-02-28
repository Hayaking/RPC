package com.haya.rpc;

import com.haya.service.IProductService;
import com.haya.service.IUserService;
import com.haya.service.impl.IProductServiceImpl;
import com.haya.service.impl.IUserServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author haya
 */
public class Server {
    private static final Map<String, Class<?>> MOCK_MAP = Map.ofEntries(
            entry( IUserService.class.getName(), IUserServiceImpl.class ),
            entry( IProductService.class.getName(), IProductServiceImpl.class )
    );


    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket( 8088 );
        while (true) {
            Socket client = server.accept();
            process( client );
            client.close();
        }
    }

    public static void process(Socket socket) throws Exception {
        ObjectInputStream ois = new ObjectInputStream( socket.getInputStream() );
        ObjectOutputStream oos = new ObjectOutputStream( socket.getOutputStream() );

        //为了适应客户端通用化而做的改动
        String clazzName = ois.readUTF();
        String methodName = ois.readUTF();
        Class[] parameterTypes = (Class[]) ois.readObject();
        Object[] parameters = (Object[]) ois.readObject();

        Object service = MOCK_MAP.get( clazzName ).newInstance();
        Method method = service.getClass().getMethod( methodName, parameterTypes );
        Object o = method.invoke( service, parameters );
        oos.writeObject( o );
        oos.flush();
    }
}
