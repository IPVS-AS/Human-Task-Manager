package com.htm.endpoint.impl;

import com.htm.endpoint.UsersService;
import com.htm.exceptions.HumanTaskManagerException;
import com.htm.security.IUserManager;
import com.htm.security.UserManagerBasicImpl;
import com.htm.userdirectory.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;


@Service
@Configurable
public class UsersServiceImpl implements UsersService {

    @Autowired
    protected IUserManager iUserManager;

    private final String DUMMY_PASSWORD = "password";


    @Override
    public String createUser(String json) {
        IUser user;
        System.out.println("am anfang der neuen Create user methode");
        try {
            user = iUserManager.addUser("Dean79", "Dean", "Winchester", DUMMY_PASSWORD);
            System.out.println(user.getFirstName());
            return user.getId();
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    @Override
    public String getUser(String id) {

        try {
            return iUserManager.getUser(id).toString();
        } catch (HumanTaskManagerException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public String getAllUsers() {
        return null;
    }

    @Override
    public String updateUser(String json, String id) {
        return null;
    }

    @Override
    public String deleteUser(String id) {
        return null;
    }


//    public void setiUserManager(IUserManager iUserManager) {
//        this.iUserManager = iUserManager;
//    }
//
//    public IUserManager getiUserManager() {return iUserManager;}
}
