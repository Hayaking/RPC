package com.haya.service.impl;

import com.haya.entity.User;
import com.haya.service.IUserService;

/**
 * @author haya
 */
public class IUserServiceImpl implements IUserService {
    @Override
    public User findUserById(int id) {
        return new User(id,"Alice");
    }
}
