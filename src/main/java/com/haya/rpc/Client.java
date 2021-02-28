package com.haya.rpc;

import com.haya.service.IProductService;
import com.haya.service.IUserService;

import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) throws InterruptedException {

        IUserService service = (IUserService) Stub.getStub(IUserService.class);
        IProductService service2 = (IProductService)Stub.getStub(IProductService.class);
        TimeUnit.SECONDS.sleep( 5 );
        System.out.println(service.findUserById(123));
        System.out.println(service2.findProductByName("Bob"));
    }
}
